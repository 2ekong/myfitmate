package com.myfitmate.myfitmate.domain.meal.repository;

import com.myfitmate.myfitmate.domain.meal.entity.Meal;
import com.myfitmate.myfitmate.domain.meal.entity.MealImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MealImageRepository extends JpaRepository<MealImage, Long> {

    Optional<MealImage> findByMeal(Meal meal);
}
