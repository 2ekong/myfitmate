package com.myfitmate.myfitmate.domain.meal.repository;

import com.myfitmate.myfitmate.domain.meal.entity.Meal;
import com.myfitmate.myfitmate.domain.meal.entity.MealType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {

    boolean existsByUserIdAndEatTimeBetweenAndMealType(Long userId, LocalDateTime start, LocalDateTime end, MealType mealType);

    List<Meal> findByUserIdAndEatTimeBetweenOrderByEatTimeAsc(Long userId, LocalDateTime start, LocalDateTime end);
}
