package org.example.foodbudgetbackendspring.recipe.mapper;

import lombok.RequiredArgsConstructor;
import org.example.foodbudgetbackendspring.product.model.Product;
import org.example.foodbudgetbackendspring.product.repository.ProductRepository;
import org.example.foodbudgetbackendspring.recipe.dto.*;
import org.example.foodbudgetbackendspring.recipe.model.Ingredient;
import org.example.foodbudgetbackendspring.recipe.model.Recipe;
import org.example.foodbudgetbackendspring.recipe.service.IngredientValidationService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
@RequiredArgsConstructor
public class RecipeMapper {
    private final ProductRepository productRepository;
    private final IngredientValidationService ingredientValidationService;

    private Ingredient toEntity(IngredientRequest ingRequest){
        Ingredient ingredient = new Ingredient();
        ingredient.setQuantity(ingRequest.quantity());
        ingredient.setUnit(ingRequest.unit());
        ingredient.setProduct(
                productRepository.getReferenceById(ingRequest.productId())
        );

        return ingredient;
    }

    private IngredientResponse toResponse(Ingredient ingredient){
        Product product = ingredient.getProduct();

        return new IngredientResponse(
                ingredient.getId(),
                ingredient.getProduct().getId(),
                ingredient.getQuantity(),
                ingredient.getUnit(),
                BigDecimal.valueOf(
                        ingredient.getQuantity() / product.getQuantity() * product.getPrice().doubleValue()
                )
        );
    }

    public Recipe toEntity(RecipeRequest request) {
        Recipe recipe = new Recipe();
        recipe.setName(request.name());
        recipe.setDescription(request.description());

        for (IngredientRequest ingredientRequest : request.ingredients()) {
            recipe.addIngredient(
                    ingredientValidationService.validate(
                            toEntity(ingredientRequest)
                    )
            );
        }

        return recipe;
    }

    public RecipeResponse toResponse(Recipe recipe) {
        return new RecipeResponse(
                recipe.getId(),
                recipe.getName(),
                recipe.getDescription(),
                recipe.getIngredients().stream().map(this::toResponse).toList()
        );
    }

    public void patchRecipe(RecipePathRequest request, Recipe recipe) {
        if (request.name() != null) recipe.setName(request.name());
        if (request.description() != null) recipe.setDescription(request.description());

        recipe.getIngredients().clear();

        for (IngredientRequest ingredientRequest : request.ingredients()) {
            recipe.addIngredient(
                    ingredientValidationService.validate(
                            toEntity(ingredientRequest)
                    )
            );
        }
    }
}
