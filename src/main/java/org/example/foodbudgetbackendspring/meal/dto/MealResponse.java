package org.example.foodbudgetbackendspring.meal.dto;

import org.example.foodbudgetbackendspring.meal.MealType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record MealResponse(
        UUID id,
        List<MealItemResponse> mealItems,
        MealType mealType,
        LocalDate date
) {
}
