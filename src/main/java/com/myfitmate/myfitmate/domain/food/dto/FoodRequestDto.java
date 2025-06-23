package com.myfitmate.myfitmate.domain.food.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FoodRequestDto {
    private String name;
    private String originCategory;
    private String originSubCategory;
    private String originDetailCategory;
    private Float standardAmount;
    private Float calories;
    private Float protein;
    private Float fat;
    private Float carbohydrate;
    private Float sodium;
    private String referenceBasis;
}
