package org.example.foodbudgetbackendspring.product.service;

import jakarta.validation.ValidationException;
import org.example.foodbudgetbackendspring.product.model.MeasurementUnit;
import org.example.foodbudgetbackendspring.product.model.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductValidationServiceTest {
    private final ProductValidationService service = new ProductValidationService();

    private Product createTestProduct() {
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

    @Test
    void shouldThrowExceptionWhenSugarGreaterThanCarbohydrates(){
        Product product = createTestProduct();
        product.setCarbohydrates(10f);
        product.setSugars(15f);

        assertThrows(ValidationException.class, () -> service.validate(product));
    }

    @Test
    void shouldThrowExceptionWhenSaturatedFatGreaterThanFat() {
        Product product = createTestProduct();
        product.setFat(5f);
        product.setSaturatedFat(7f);

        assertThrows(ValidationException.class, () -> service.validate(product));
    }

    @Test
    void shouldThrowExceptionWhenDensityProvidedForMassUnit() {
        Product product = createTestProduct();
        product.setNutrientUnit(MeasurementUnit.GRAM);
        product.setDensity(1.05f);

        assertThrows(ValidationException.class, () -> service.validate(product));
    }

    @Test
    void shouldThrowExceptionForInvalidEAN(){
        Product product = createTestProduct();
        product.setEan("5000112677868");

        assertThrows(ValidationException.class, () -> service.validate(product));
    }

    @Test
    void shouldAcceptValidEAN(){
        Product product = createTestProduct();
        product.setEan("5000112677867");

        service.validate(product);
    }
}