package com.myfitmate.myfitmate.domain.meal.repository;

import com.myfitmate.myfitmate.domain.meal.entity.MealImage;
import com.myfitmate.myfitmate.domain.meal.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MealImageRepository extends JpaRepository<MealImage, Long> {
    Optional<MealImage> findByMealId(Long mealId);

}
