package com.myfitmate.myfitmate.domain.meal.dto;

import com.myfitmate.myfitmate.domain.food.entity.Food;
import com.myfitmate.myfitmate.domain.meal.entity.Meal;
import com.myfitmate.myfitmate.domain.meal.entity.MealFood;
import com.myfitmate.myfitmate.domain.meal.entity.MealImage;
import com.myfitmate.myfitmate.domain.meal.entity.MealType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class MealResponseDto {

    private Long id;
    private Long userId;
    private LocalDateTime eatTime;
    private MealType mealType;
    private float totalCalories;
    private boolean hasImage;
    private String imageUrl;
    private List<FoodDetail> foodList;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class FoodDetail {
        private Long foodId;
        private String foodName;
        private float quantity;
        private float calories;
    }

    public static MealResponseDto fromEntity(Meal meal, List<MealFood> mealFoods, MealImage mealImage) {
        List<FoodDetail> foodList = mealFoods.stream().map(mf -> {
            Food food = mf.getFood();
            return FoodDetail.builder()
                    .foodId(food.getId())
                    .foodName(food.getName())
                    .quantity(mf.getQuantity())
                    .calories(mf.getCalories())
                    .build();
        }).collect(Collectors.toList());

        return MealResponseDto.builder()
                .id(meal.getId())
                .userId(meal.getUserId())
                .eatTime(meal.getEatTime())
                .mealType(meal.getMealType())
                .totalCalories(meal.getTotalCalories())
                .hasImage(meal.isHasImage())
                .imageUrl(mealImage != null ? mealImage.getFilePath() : null)
                .foodList(foodList)
                .build();
    }
}
