package com.myfitmate.myfitmate.domain.food.dto;

import com.myfitmate.myfitmate.domain.food.entity.Food;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FoodResponseDto {

    private Long id;
    private String name;
    private String originCategory;
    private String originSubCategory;
    private String originDetailCategory;
    private Float standardAmount;
    private Float calories;
    private Float carbohydrate;
    private Float protein;
    private Float fat;
    private Float sodium;
    private String referenceBasis;

    @Builder
    public FoodResponseDto(Long id, String name, String originCategory,
                           String originSubCategory, String originDetailCategory,
                           Float standardAmount, Float calories, Float carbohydrate,
                           Float protein, Float fat, Float sodium, String referenceBasis) {
        this.id = id;
        this.name = name;
        this.originCategory = originCategory;
        this.originSubCategory = originSubCategory;
        this.originDetailCategory = originDetailCategory;
        this.standardAmount = standardAmount;
        this.calories = calories;
        this.carbohydrate = carbohydrate;
        this.protein = protein;
        this.fat = fat;
        this.sodium = sodium;
        this.referenceBasis = referenceBasis;
    }

    public static FoodResponseDto fromEntity(Food food) {
        return FoodResponseDto.builder()
                .id(food.getId())
                .name(food.getName())
                .originCategory(food.getOriginCategory())
                .originSubCategory(food.getOriginSubCategory())
                .originDetailCategory(food.getOriginDetailCategory())
                .standardAmount(food.getStandardAmount())
                .calories(food.getCalories())
                .carbohydrate(food.getCarbohydrate())
                .protein(food.getProtein())
                .fat(food.getFat())
                .sodium(food.getSodium())
                .referenceBasis(food.getReferenceBasis())
                .build();
    }
}
