package com.myfitmate.myfitmate.domain.meal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@IdClass(MealFoodId.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "meal_food")
public class MealFood {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    @Id
    @Column(name = "food_id", nullable = false)
    private Long foodId;

    @Column(nullable = false)
    private Float quantity;

    @Column(nullable = false)
    private Float calories;
}
