package org.example.foodbudgetbackendspring.recipe.service;

import org.example.foodbudgetbackendspring.product.model.MeasurementUnit;
import org.example.foodbudgetbackendspring.product.model.Product;
import org.example.foodbudgetbackendspring.recipe.model.Ingredient;
import org.example.foodbudgetbackendspring.utils.TestDataFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IngredientValidationServiceTest {

    @Test
    void validateTestNullDensity() {
        Product product = TestDataFactory.createTestProduct();
        product.setDensity(null);
        product.setNutrientUnit(MeasurementUnit.MILLILITER);

        Ingredient ingredient = TestDataFactory.createTestIngredient(product);
        ingredient.setUnit(MeasurementUnit.MILLILITER);
    }
}