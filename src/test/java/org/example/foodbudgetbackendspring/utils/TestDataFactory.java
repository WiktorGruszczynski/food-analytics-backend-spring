package org.example.foodbudgetbackendspring.utils;

import org.example.foodbudgetbackendspring.product.model.MeasurementUnit;
import org.example.foodbudgetbackendspring.product.model.Product;
import org.example.foodbudgetbackendspring.recipe.model.Ingredient;

public class TestDataFactory {
    public static Product createTestProduct() {
        Product product = new Product();
        product.setName("Example product");
        product.setFat(0f);
        product.setSaturatedFat(0f);
        product.setCarbohydrates(0f);
        product.setSugars(0f);
        product.setFiber(0f);
        product.setProtein(0f);

        return product;
    }

    public static Ingredient createTestIngredient(Product product) {
        Ingredient ingredient = new Ingredient();
        ingredient.setQuantity(0f);
        ingredient.setUnit(MeasurementUnit.GRAM);
        ingredient.setProduct(product);
        return ingredient;
    }
}
