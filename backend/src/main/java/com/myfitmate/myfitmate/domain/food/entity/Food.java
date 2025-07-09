package com.myfitmate.myfitmate.domain.food.entity;

import com.myfitmate.myfitmate.domain.food.dto.FoodRequestDto;
import com.myfitmate.myfitmate.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public static Food fromDto(FoodRequestDto dto) {
        Food food = new Food();
        food.name = dto.getName();
        food.originCategory = dto.getOriginCategory();
        food.originSubCategory = dto.getOriginSubCategory();
        food.originDetailCategory = dto.getOriginDetailCategory();
        food.standardAmount = dto.getStandardAmount();
        food.calories = dto.getCalories();
        food.carbohydrate = dto.getCarbohydrate();
        food.protein = dto.getProtein();
        food.fat = dto.getFat();
        food.sodium = dto.getSodium();
        food.referenceBasis = dto.getReferenceBasis();
        return food;
    }

    public void updateFromDto(FoodRequestDto dto) {
        this.name = dto.getName();
        this.originCategory = dto.getOriginCategory();
        this.originSubCategory = dto.getOriginSubCategory();
        this.originDetailCategory = dto.getOriginDetailCategory();
        this.standardAmount = dto.getStandardAmount();
        this.calories = dto.getCalories();
        this.carbohydrate = dto.getCarbohydrate();
        this.protein = dto.getProtein();
        this.fat = dto.getFat();
        this.sodium = dto.getSodium();
        this.referenceBasis = dto.getReferenceBasis();
    }
}
