package com.myfitmate.myfitmate.domain.meal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MealFoodRequestDto {
    private Long foodId;
    private Float quantity;
}
