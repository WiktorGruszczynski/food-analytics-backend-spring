package org.example.foodbudgetbackendspring.recipe.dto;

import java.util.List;

public record RecipeResponse(
        Long id,
        String name,
        String description,
        List<IngredientResponse> ingredients
) {
}
