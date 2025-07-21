package com.myfitmate.myfitmate.domain.meal.dto;

import com.myfitmate.myfitmate.domain.meal.entity.MealType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class MealRequestDto {

    private LocalDateTime eatTime;
    private MealType mealType;
    private List<FoodInfo> foodList;

    @Getter
    @NoArgsConstructor
    public static class FoodInfo {
        private Long foodId;
        private float quantity;

        public Long getFoodId() {
            return foodId;
        }

        public float getQuantity() {
            return quantity;
        }
    }
}
