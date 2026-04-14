package org.example.foodbudgetbackendspring.meal.dto;

import org.example.foodbudgetbackendspring.meal.MealType;
import org.example.foodbudgetbackendspring.product.model.MeasurementUnit;

import java.time.LocalDate;
import java.util.UUID;

public record MealItemRequest(
        UUID productId,
        float quantity,
        MeasurementUnit unit,
        MealType mealType,
        LocalDate date
) {
}
