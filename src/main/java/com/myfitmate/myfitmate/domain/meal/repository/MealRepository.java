package com.myfitmate.myfitmate.domain.meal.repository;

import com.myfitmate.myfitmate.domain.meal.entity.Meal;
import com.myfitmate.myfitmate.domain.meal.entity.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {

    boolean existsByUserIdAndEatTimeBetweenAndMealType(
            Long userId, LocalDateTime start, LocalDateTime end, MealType mealType);

    List<Meal> findByUserId(Long userId);

    @Query("""
        SELECT DISTINCT m FROM Meal m 
        LEFT JOIN MealFood mf ON mf.meal = m
        LEFT JOIN Food f ON mf.food = f
        WHERE m.userId = :userId AND (
            LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(CAST(m.mealType AS string)) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    """)
    List<Meal> findByUserIdAndKeyword(@Param("userId") Long userId, @Param("keyword") String keyword);
}
