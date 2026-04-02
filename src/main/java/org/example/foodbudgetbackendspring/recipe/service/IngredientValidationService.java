package org.example.foodbudgetbackendspring.recipe.service;

import jakarta.validation.ValidationException;
import org.example.foodbudgetbackendspring.product.model.MeasurementUnit;
import org.example.foodbudgetbackendspring.product.model.Product;
import org.example.foodbudgetbackendspring.recipe.model.Ingredient;
import org.springframework.stereotype.Service;


@Service
public class IngredientValidationService {
    private void validateUnits(Ingredient ingredient) {
        Product product = ingredient.getProduct();
        MeasurementUnit unit = ingredient.getUnit();

        if (unit == MeasurementUnit.MILLILITER && product.getDensity() == null) {
            throw new ValidationException(
                    "Product " + product.getName() + " cannot be measured in ML without density value"
            );
        }
    }

    public Ingredient validate(Ingredient ingredient) {
        validateUnits(ingredient);

        return ingredient;
    }
}
