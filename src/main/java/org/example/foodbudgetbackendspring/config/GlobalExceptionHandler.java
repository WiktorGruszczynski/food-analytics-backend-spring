package org.example.foodbudgetbackendspring.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntime(RuntimeException ex){
        System.err.println("Business error: " + ex.getMessage());

        // return 400 bad request
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
