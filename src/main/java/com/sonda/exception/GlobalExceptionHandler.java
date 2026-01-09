package com.sonda.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(BadResourceRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadResourceRequest(BadResourceRequestException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());  // "Model with same id exists."
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(TablasReferenciasException.class)
    public ResponseEntity<Map<String, String>> handleBadResourceRequest(TablasReferenciasException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());  // "Model with same id exists."
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
