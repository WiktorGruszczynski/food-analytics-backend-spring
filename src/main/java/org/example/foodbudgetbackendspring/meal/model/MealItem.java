package org.example.foodbudgetbackendspring.meal.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.foodbudgetbackendspring.product.model.MeasurementUnit;
import org.example.foodbudgetbackendspring.product.model.Product;

import java.util.UUID;

@Entity
@Table(name = "meal_items")
@Data
@NoArgsConstructor
public class MealItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "meal_id", nullable = false)
    @ToString.Exclude
    private Meal meal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude
    private Product product;

    private float quantity;

    @Enumerated(EnumType.STRING)
    private MeasurementUnit unit;
}
