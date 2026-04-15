package org.example.foodbudgetbackendspring.meal.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.example.foodbudgetbackendspring.meal.MealType;
import org.example.foodbudgetbackendspring.product.model.MeasurementUnit;

import java.time.LocalDate;
import java.util.UUID;

public record MealItemRequest(
        @NotNull UUID productId,
        @Positive float quantity,
        @NotNull MeasurementUnit unit,
        @NotNull MealType mealType,
        @NotNull LocalDate date
) {
}
