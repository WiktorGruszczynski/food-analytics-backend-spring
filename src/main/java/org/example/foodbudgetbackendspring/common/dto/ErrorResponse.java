package org.example.foodbudgetbackendspring.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ErrorResponse(
        String message,
        int status,
        LocalDateTime timestamp,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Map<String, List<String>> errors
) {
    public ErrorResponse(String message, HttpStatus status, Map<String, List<String>> errors){
        this(message, status.value(), LocalDateTime.now(), errors);
    }
}
