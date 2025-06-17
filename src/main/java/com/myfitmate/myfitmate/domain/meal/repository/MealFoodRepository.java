package com.myfitmate.myfitmate.domain.meal.repository;

import com.myfitmate.myfitmate.domain.meal.entity.MealFood;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealFoodRepository extends JpaRepository<MealFood, Long> {
    void deleteByMealId(Long mealId);
}
