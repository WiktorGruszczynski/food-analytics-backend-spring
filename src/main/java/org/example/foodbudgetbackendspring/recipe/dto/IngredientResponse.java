package org.example.foodbudgetbackendspring.recipe.dto;

import org.example.foodbudgetbackendspring.product.model.MeasurementUnit;

public record IngredientResponse(
        Long id,
        Long productId,
        Float quantity,
        MeasurementUnit unit
) {
}
