package com.myfitmate.myfitmate.domain.food.dto;

import com.myfitmate.myfitmate.domain.food.entity.Food;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FoodResponseDto {

    private Long id;
    private String name;
    private Float calories;
    private Float carbohydrate;
    private Float protein;
    private Float fat;

    @Builder
    public FoodResponseDto(Long id, String name, Float calories, Float carbohydrate, Float protein, Float fat) {
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

    public static FoodResponseDto ok(String message) {
        return FoodResponseDto.builder()
                .id(null)
                .name(message)
                .calories(0f)
                .carbohydrate(0f)
                .protein(0f)
                .fat(0f)
                .build();
    }

}
