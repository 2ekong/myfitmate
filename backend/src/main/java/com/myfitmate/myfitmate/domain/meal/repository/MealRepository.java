package com.myfitmate.myfitmate.domain.meal.repository;

import com.myfitmate.myfitmate.domain.meal.entity.Meal;
import com.myfitmate.myfitmate.domain.meal.entity.MealType;
import com.myfitmate.myfitmate.domain.statistics.dto.DailyCalorieDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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

    // 주간 식사 칼로리 합계 (일별)
    @Query("""
        SELECT new com.myfitmate.myfitmate.domain.statistics.dto.DailyCalorieDto(
            DATE(m.eatTime), SUM(m.totalCalories)
        )
        FROM Meal m
        WHERE m.userId = :userId AND m.eatTime >= :startDate
        GROUP BY DATE(m.eatTime)
    """)
    List<DailyCalorieDto> findDailyMealCalories(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate
    );
}
