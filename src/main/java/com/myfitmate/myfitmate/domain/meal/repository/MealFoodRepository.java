package com.myfitmate.myfitmate.domain.meal.repository;

import com.myfitmate.myfitmate.domain.meal.entity.Meal;
import com.myfitmate.myfitmate.domain.meal.entity.MealFood;
import com.myfitmate.myfitmate.domain.meal.entity.MealFoodId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealFoodRepository extends JpaRepository<MealFood, MealFoodId> {

    List<MealFood> findByMeal(Meal meal);

    void deleteByMeal(Meal meal);
}
