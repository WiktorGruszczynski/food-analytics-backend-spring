package org.example.foodbudgetbackendspring.recipe.dto;



import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;


import java.util.List;

public record RecipeRequest(
        @NotBlank String name,
        @NotBlank String description,
        @Valid @NotEmpty List<IngredientRequest> ingredients
) {
}
