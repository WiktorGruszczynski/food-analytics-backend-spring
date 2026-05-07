package org.example.foodbudgetbackendspring.core.auth.exception;

public class InvalidVerificationToken extends RuntimeException {
    public InvalidVerificationToken(String message) {
        super(message);
    }
}
