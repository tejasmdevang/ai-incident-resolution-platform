package com.tejas.incidentplatform.exception;

import java.time.OffsetDateTime;
import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IncidentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleIncidentNotFound(
        IncidentNotFoundException exception){

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("timestamp", OffsetDateTime.now());
            body.put("status", HttpStatus.NOT_FOUND.value());
            body.put("error", "Not Found");
            body.put("message", exception.getMessage());

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(body);
        }

}
