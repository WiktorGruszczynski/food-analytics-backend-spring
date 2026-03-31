package org.example.foodbudgetbackendspring.recipe.repository;

import org.example.foodbudgetbackendspring.recipe.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("select r from Recipe r left join fetch r.ingredients where r.id = :id")
    Optional<Recipe> findByIdWithIngredients(@Param("id") Long id);

    @Query("select r from Recipe r " +
            "left join fetch r.ingredients i " +
            "left join fetch i.product " +
            "where r.id = :id")
    Optional<Recipe> findByIdWithIngredientsAndProducts(@Param("id") Long id);
}
