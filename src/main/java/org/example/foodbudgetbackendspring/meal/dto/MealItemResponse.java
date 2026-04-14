package org.example.foodbudgetbackendspring.meal.dto;

import org.example.foodbudgetbackendspring.product.dto.ProductResponse;
import org.example.foodbudgetbackendspring.product.model.MeasurementUnit;

public record MealItemResponse(
        ProductResponse product,
        float quantity,
        MeasurementUnit unit
) {
}
