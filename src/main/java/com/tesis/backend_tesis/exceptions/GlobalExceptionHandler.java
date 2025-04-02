package com.tesis.backend_tesis.exceptions;

import com.tesis.backend_tesis.controller.VistaRestFullController;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(
            ResponseStatusException ex, HttpServletRequest request) {

        // Registrar el error en los logs
        //logger.error("Error en la solicitud: {} - {} - {}", ex.getStatusCode().value(), ex.getReason(), request.getRequestURI());

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", ex.getStatusCode().value());
        response.put("error", ex.getReason());
        response.put("path", request.getRequestURI());

        return new ResponseEntity<>(response, ex.getStatusCode());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParams(MissingServletRequestParameterException ex, HttpServletRequest request) {
        // Registrar el error en los logs
        logger.error("Parámetro faltante: '{}' en la solicitud: {}", ex.getParameterName(), request.getRequestURI());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", 400);  // Código de error para Bad Request
        errorResponse.put("error", "El parámetro '" + ex.getParameterName() + "' es obligatorio.");
        errorResponse.put("path", request.getRequestURI());

        return new ResponseEntity<>(errorResponse, org.springframework.http.HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        // Registrar el error en los logs
        logger.error("Error de tipo en el parámetro: '{}' con valor: '{}' en la solicitud: {}", ex.getName(), ex.getValue(), request.getRequestURI());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", 400); // El código HTTP para Bad Request
        errorResponse.put("error", "El parámetro '" + ex.getName() + "' tiene un valor inválido: '" + ex.getValue() + "'. Debe ser del tipo: " + ex.getRequiredType().getSimpleName());
        errorResponse.put("path", request.getRequestURI());

        return new ResponseEntity<>(errorResponse, org.springframework.http.HttpStatus.BAD_REQUEST);
    }
}
