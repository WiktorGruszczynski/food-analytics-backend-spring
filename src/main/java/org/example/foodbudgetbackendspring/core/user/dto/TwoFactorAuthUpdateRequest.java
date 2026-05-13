package org.example.foodbudgetbackendspring.core.user.dto;

public record TwoFactorAuthUpdateRequest(
        boolean twoFactorAuthEnabled
) {
}
