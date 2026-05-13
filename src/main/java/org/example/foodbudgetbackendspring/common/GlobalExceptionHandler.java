package org.example.foodbudgetbackendspring.common;

import lombok.extern.slf4j.Slf4j;
import org.example.foodbudgetbackendspring.common.dto.ErrorResponse;
import org.example.foodbudgetbackendspring.core.auth.exception.InvalidVerificationToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus status, String message, Map<String, List<String>> errors) {
        return ResponseEntity
                .status(status)
                .body(
                        new ErrorResponse(message, status, errors)
                );
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus status, String message) {
        return createErrorResponse(status, message, null);
    }

    private Map<String, List<String>> generateValidationErrors(MethodArgumentNotValidException e) {
        return e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        return createErrorResponse(
                HttpStatus.UNAUTHORIZED, "Bad Credentials"
        );
    }

    @ExceptionHandler(InvalidVerificationToken.class)
    public ResponseEntity<ErrorResponse> handleInvalidVerificationToken(InvalidVerificationToken e) {
        return createErrorResponse(
                HttpStatus.UNAUTHORIZED, "Invalid Verification Token"
        );
    }

    @ExceptionHandler({
                    AuthorizationDeniedException.class,
                    InsufficientAuthenticationException.class
    })
    public ResponseEntity<ErrorResponse> unauthorizedException(AuthorizationDeniedException e) {
        return createErrorResponse(
                HttpStatus.UNAUTHORIZED, "Unauthorized"
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        return createErrorResponse(
                HttpStatus.BAD_REQUEST, "Validation error", generateValidationErrors(e)
        );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabledException(DisabledException e) {
        return createErrorResponse(
                HttpStatus.FORBIDDEN, e.getMessage()
        );
    }
}
