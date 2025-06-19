package com.myfitmate.myfitmate.domain.meal.entity;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class MealFoodId implements Serializable {

    private Long meal;
    private Long foodId;

    public MealFoodId() {}
}
