package com.myfitmate.myfitmate.domain.meal.dto;

import com.myfitmate.myfitmate.domain.meal.entity.MealType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MealRequestDto {

    @NotNull
    private LocalDateTime eatTime;

    @NotNull
    private MealType mealType;

    @NotNull
    private List<FoodItem> foodList;

    public record FoodItem(
            @NotNull Long foodId,
            float quantity
    ) {}
}
