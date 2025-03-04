package com.distribuida.login.exceptions;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;



@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", ex.getStatusCode().value());
        response.put("error", ex.getReason()); // Aquí sí se devuelve el mensaje personalizado
        response.put("path", "/API/tesis/auth/registro"); // Opcional: lo puedes obtener dinámicamente

        return new ResponseEntity<>(response, ex.getStatusCode());
    }
}
