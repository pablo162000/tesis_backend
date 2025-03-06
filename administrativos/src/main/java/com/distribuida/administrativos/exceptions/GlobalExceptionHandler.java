package com.distribuida.administrativos.exceptions;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Manejo de ResponseStatusException (errores de estado HTTP)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", ex.getStatusCode().value());
        response.put("error", ex.getReason());
        response.put("path", request.getRequestURI()); // Usamos HttpServletRequest para obtener la URL de la solicitud

        return new ResponseEntity<>(response, ex.getStatusCode());
    }

    // Manejo de FeignException (errores de comunicación con servicio remoto)
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, Object>> handleFeignException(FeignException ex, HttpServletRequest request) {
        String errorMessage = ex.getMessage();
        HttpStatus status = ex.status() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR;

        // Ajuste para mensajes específicos de conflicto
        if (errorMessage.contains("Ya existe un usuario registrado con este correo") ||
                errorMessage.contains("La cédula ya está registrada")) {
            status = HttpStatus.CONFLICT;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", errorMessage);
        response.put("path", request.getRequestURI()); // Usamos HttpServletRequest para obtener la URL de la solicitud

        return new ResponseEntity<>(response, status);
    }
}
