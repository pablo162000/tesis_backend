package com.tesis.backend_tesis.utilitarios;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Validaciones {

    private Validaciones() {
    }

    public static boolean esCorreoValido(String correo) {
        return correo != null && correo.toLowerCase().endsWith("@uce.edu.ec");
    }

    public String obtenerNombresEstudiantes(List<String> posiblesNombres) {
        // Filtrar nombres nulos o vacíos
        List<String> nombresValidos = posiblesNombres.stream()
                .filter(nombre -> nombre != null && !nombre.trim().isEmpty())
                .collect(Collectors.toList());

        return formatearListaConY(nombresValidos);
    }

    private String formatearListaConY(List<String> elementos) {
        if (elementos.isEmpty()) {
            return "";
        } else if (elementos.size() == 1) {
            return elementos.get(0);
        } else {
            return String.join(", ", elementos.subList(0, elementos.size() - 1)) +
                    " y " + elementos.get(elementos.size() - 1);
        }
    }

    public String sumarDiasLaborables(LocalDate fechaInicio, int diasLaborables) {
        LocalDate fecha = fechaInicio;
        int diasContados = 0;

        while (diasContados < diasLaborables) {
            fecha = fecha.plusDays(1);

            if (fecha.getDayOfWeek() != DayOfWeek.SATURDAY &&
                    fecha.getDayOfWeek() != DayOfWeek.SUNDAY) {
                diasContados++;
            }
        }

        // Formatear como string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return fecha.format(formatter);
    }

    public String diasTimer(LocalDate fechaInicio, String fechaFin) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate fechaFinal = LocalDate.parse(fechaFin, formatter);

        long diasEntre = ChronoUnit.DAYS.between(fechaInicio, fechaFinal);

        if (diasEntre < 0) {
            throw new IllegalArgumentException("La fecha final debe ser posterior a la fecha de inicio.");
        }

        String formato = "P" + diasEntre + "D";
        return formato;
    }





    public void validarArchivo(MultipartFile archivo, String nombreCampo, List<String> tiposPermitidos) {
        if (archivo == null || archivo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha proporcionado el archivo: " + nombreCampo);
        }

        String contentType = archivo.getContentType();
        if (contentType == null || tiposPermitidos.stream().noneMatch(contentType::equals)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato inválido para el archivo: " + nombreCampo);
        }
    }

    public String generarPeriodo(){

        LocalDate fechaActual = LocalDate.now();
        int year = fechaActual.getYear();

        // Periodo 1: 20 mar - 20 ago del año actual
        LocalDate inicioP1 = LocalDate.of(year, 3, 20);
        LocalDate finP1    = LocalDate.of(year, 8, 20);

        // Periodo 2: 21 ago (del año anterior) - 19 mar (del año actual)
        LocalDate inicioP2 = LocalDate.of(year - 1, 8, 21);
        LocalDate finP2    = LocalDate.of(year, 3, 19);

        if (!fechaActual.isBefore(inicioP1) && !fechaActual.isAfter(finP1)) {
            return year + "-" + year;
        } else if (!fechaActual.isBefore(inicioP2) && !fechaActual.isAfter(finP2)) {
            return (year - 1) + "-" + year;
        }

        return "-";
    }
}
