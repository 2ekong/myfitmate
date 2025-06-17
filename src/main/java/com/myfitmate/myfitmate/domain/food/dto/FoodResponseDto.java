package com.myfitmate.myfitmate.domain.food.dto;

import com.myfitmate.myfitmate.domain.food.entity.Food;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FoodResponseDto {

    private Long id;
    private String name;
    private double calories;
    private double carbohydrate;
    private double protein;
    private double fat;

    @Builder
    public FoodResponseDto(Long id, String name, double calories, double carbohydrate, double protein, double fat) {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.carbohydrate = carbohydrate;
        this.protein = protein;
        this.fat = fat;
    }

    public static FoodResponseDto fromEntity(Food food) {
        return FoodResponseDto.builder()
                .id(food.getId())
                .name(food.getName())
                .calories(food.getCalories())
                .carbohydrate(food.getCarbohydrate())
                .protein(food.getProtein())
                .fat(food.getFat())
                .build();
    }
}
