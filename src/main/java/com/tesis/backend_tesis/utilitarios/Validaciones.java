package com.tesis.backend_tesis.utilitarios;

import org.springframework.stereotype.Service;

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
}
