package org.example.foodbudgetbackendspring.meal;

import org.example.foodbudgetbackendspring.meal.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
interface MealRepository extends JpaRepository<Meal, UUID> {

    @Query("SELECT DISTINCT m FROM Meal m " +
            "LEFT JOIN FETCH m.mealItems mi " +
            "LEFT JOIN FETCH mi.product " +
            "WHERE m.user.id = :userId " +
            "AND m.date BETWEEN :startDate AND :endDate")
    List<Meal> findAllByUserIdWithItemsAndProducts(
            @Param("userId") UUID userId,
            @Param("startDate") LocalDate startDate,
            @Param("end") LocalDate endDate
    );

    Optional<Meal> findByUserIdAndDateAndType(UUID mealId, LocalDate date, MealType mealType);
}
