package org.example.foodbudgetbackendspring.meal;

import lombok.RequiredArgsConstructor;
import org.example.foodbudgetbackendspring.meal.dto.MealItemRequest;
import org.example.foodbudgetbackendspring.meal.dto.MealResponse;
import org.example.foodbudgetbackendspring.user.model.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/meal")
@RequiredArgsConstructor
public class MealController {
    private final MealService mealService;

    @GetMapping
    public ResponseEntity<List<MealResponse>> getUserMealsFromRange(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate
    ){
        return ResponseEntity.ok(
                mealService.getUserMealsFromRange(userDetails.getId(), startDate, endDate)
        );
    }

    @PostMapping
    public ResponseEntity<?> addMealItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MealItemRequest request
    ){
        return ResponseEntity
                .status(201)
                .body(
                        mealService.addMealItem(userDetails.getId(), request)
                );
    }
}
