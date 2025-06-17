package com.myfitmate.myfitmate.domain.meal.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MealFoodId implements Serializable {
    private Long meal;     // Meal 객체의 id (Entity의 필드명과 정확히 일치)
    private Long foodId;   // 기본형 필드

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MealFoodId that)) return false;
        return Objects.equals(meal, that.meal) && Objects.equals(foodId, that.foodId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meal, foodId);
    }

}
