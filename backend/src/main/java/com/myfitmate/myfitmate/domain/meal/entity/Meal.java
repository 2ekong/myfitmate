package com.myfitmate.myfitmate.domain.meal.entity;

import com.myfitmate.myfitmate.domain.meal.entity.MealType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private LocalDateTime eatTime;

    @Enumerated(EnumType.STRING)
    private MealType mealType;

    private float totalCalories;

    private int version;

    private boolean hasImage;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updateTotalCalories(float totalCalories) {
        this.totalCalories = totalCalories;
    }

    public void updateMeal(LocalDateTime eatTime, MealType mealType, float totalCalories, boolean hasImage) {
        this.eatTime = eatTime;
        this.mealType = mealType;
        this.totalCalories = totalCalories;
        this.hasImage = hasImage;
    }
}
