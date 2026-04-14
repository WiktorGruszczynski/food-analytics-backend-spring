package org.example.foodbudgetbackendspring.meal.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.foodbudgetbackendspring.meal.MealType;
import org.example.foodbudgetbackendspring.user.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "meals")
@Data
@NoArgsConstructor
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<MealItem> mealItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private MealType type;

    private LocalDate date;

    public void addMealItem(MealItem item) {
        mealItems.add(item);
        item.setMeal(this);
    }
}
