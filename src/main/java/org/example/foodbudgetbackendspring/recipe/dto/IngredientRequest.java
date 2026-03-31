package org.example.foodbudgetbackendspring.recipe.dto;

import org.example.foodbudgetbackendspring.product.model.MeasurementUnit;

public record IngredientRequest(
        Long productId,
        Float quantity,
        MeasurementUnit unit
) {
}
