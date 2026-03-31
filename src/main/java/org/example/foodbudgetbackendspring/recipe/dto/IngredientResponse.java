package org.example.foodbudgetbackendspring.recipe.dto;

import org.example.foodbudgetbackendspring.product.model.MeasurementUnit;

import java.math.BigDecimal;

public record IngredientResponse(
        Long id,
        Long productId,
        Float quantity,
        MeasurementUnit unit,
        BigDecimal price
) {
}
