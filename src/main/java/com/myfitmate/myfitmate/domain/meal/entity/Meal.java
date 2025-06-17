package com.myfitmate.myfitmate.domain.meal.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; //  User 대신 ID만 사용

    private LocalDateTime eatTime;

    @Enumerated(EnumType.STRING)
    private MealType mealType;

    private Float totalCalories;

    private Boolean hasImage;

    private Integer version;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealFood> mealFoods = new ArrayList<>();

    public void addMealFood(Long foodId, Float quantity, Float calories) {
        MealFood mealFood = MealFood.builder()
                .meal(this)
                .foodId(foodId)
                .quantity(quantity)
                .calories(calories)
                .build();
        this.mealFoods.add(mealFood);
    }
}
