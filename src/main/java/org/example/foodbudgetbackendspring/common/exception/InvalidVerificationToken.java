package org.example.foodbudgetbackendspring.common.exception;

public class InvalidVerificationToken extends RuntimeException {
    public InvalidVerificationToken(String message) {
        super(message);
    }
}
