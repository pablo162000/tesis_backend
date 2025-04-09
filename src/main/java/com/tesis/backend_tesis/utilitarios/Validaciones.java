package com.tesis.backend_tesis.utilitarios;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public void validarArchivo(MultipartFile archivo, String nombreCampo, List<String> tiposPermitidos) {
        if (archivo == null || archivo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha proporcionado el archivo: " + nombreCampo);
        }

        String contentType = archivo.getContentType();
        if (contentType == null || tiposPermitidos.stream().noneMatch(contentType::equals)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato inválido para el archivo: " + nombreCampo);
        }
    }
}
