package com.myfitmate.myfitmate.domain.meal.dto;

import com.myfitmate.myfitmate.domain.meal.entity.MealType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MealResponseDto {
    private Long id;
    private LocalDateTime eatTime;
    private MealType mealType;
    private float totalCalories;
    private boolean hasImage;
    private List<FoodDetail> foodList;
    private String imageUrl;

    public MealResponseDto(Long id, LocalDateTime eatTime, MealType mealType,
                           float totalCalories, Boolean hasImage, List<FoodDetail> foodList, String imageUrl) {
        this.id = id;
        this.eatTime = eatTime;
        this.mealType = mealType;
        this.totalCalories = totalCalories;
        this.hasImage = hasImage;
        this.foodList = foodList;
        this.imageUrl = imageUrl;
    }

    public MealResponseDto(Long id, LocalDateTime eatTime, MealType mealType,
                           float totalCalories, boolean hasImage,
                           List<FoodDetail> foodList) {
        this(id, eatTime, mealType, totalCalories, hasImage, foodList, null);
    }

    public record FoodDetail(   //dto 안에서만 쓰이는 구조이면 내부 record로 가능
                                //외부에서 재사용되지 않고, 작고 명확한 목적만 있을 때 -> 유지보수가 더 쉬움
            Long foodId,
            String foodName,
            float quantity,
            float calories
    ){}



}
