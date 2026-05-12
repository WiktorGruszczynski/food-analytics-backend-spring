package org.example.foodbudgetbackendspring.core.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LoginResponse(
    String message,
    String token,
    boolean requires2FA
) {

    public static LoginResponse requires2FA(String message) {
        return new LoginResponse(message, null, true);
    }

    public static LoginResponse success(String token) {
        return new LoginResponse("Logged in successfully", token, false);
    }
}
