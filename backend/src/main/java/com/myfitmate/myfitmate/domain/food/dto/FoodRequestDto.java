package com.myfitmate.myfitmate.domain.food.dto;

import com.myfitmate.myfitmate.domain.food.entity.Food;
import com.myfitmate.myfitmate.domain.user.entity.User;
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

    public Food toEntity(User user) {
        Food food = new Food();
        food.setUser(user);
        food.updateFromDto(this);
        return food;
    }

}