package org.example.foodbudgetbackendspring.core.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConfirmPasswordResetRequest (
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Code is required")
        String code,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max=64, message = "Password must be between 8 and 64 characters")
        String newPassword
){
}
