package com.myfitmate.myfitmate.domain.food.dto;

import lombok.Getter;

@Getter
public class FoodRequestDto {
    private String name;
    private Float standardAmount;
    private Float calories;
    private Float protein;
    private Float fat;
    private Float carbohydrate;
    private Float sodium;
    private String referenceBasis;
}
