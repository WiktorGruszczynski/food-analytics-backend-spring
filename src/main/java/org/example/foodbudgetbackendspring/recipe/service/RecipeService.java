package org.example.foodbudgetbackendspring.recipe.service;

import lombok.RequiredArgsConstructor;
import org.example.foodbudgetbackendspring.recipe.dto.RecipeRequest;
import org.example.foodbudgetbackendspring.recipe.dto.RecipeResponse;
import org.example.foodbudgetbackendspring.recipe.mapper.RecipeMapper;
import org.example.foodbudgetbackendspring.recipe.model.Recipe;
import org.example.foodbudgetbackendspring.recipe.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;

    @Transactional
    public RecipeResponse addRecipe(RecipeRequest request) {
        return recipeMapper.toResponse(
                recipeRepository.save(
                        recipeMapper.toEntity(request)
                )
        );
    }

    @Transactional(readOnly = true)
    public RecipeResponse getRecipe(Long id) {
        return recipeMapper.toResponse(
                recipeRepository
                        .findByIdWithIngredients(id)
                        .orElseThrow(
                                () -> new RuntimeException("Recipe not found")
                        )
        );
    }
}
