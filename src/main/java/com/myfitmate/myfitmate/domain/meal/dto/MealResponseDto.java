package com.myfitmate.myfitmate.domain.meal.dto;

import com.myfitmate.myfitmate.domain.meal.entity.MealType;
import java.time.LocalDateTime;
import java.util.List;

public record MealResponseDto(
        Long id,
        LocalDateTime eatTime,
        MealType mealType,
        float totalCalories,
        List<FoodDetail> foodList
) {
    public record FoodDetail(
            Long foodId,
            String name,
            float quantity,
            float calories
    ) {}
}
