package com.myfitmate.myfitmate.domain.statistics.repository;

import com.myfitmate.myfitmate.domain.meal.entity.MealFood;
import com.myfitmate.myfitmate.domain.meal.entity.MealFoodId;
import com.myfitmate.myfitmate.domain.statistics.dto.NutrientSummaryDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatisticsRepository extends CrudRepository<MealFood, MealFoodId> {

    @Query("""
    SELECT new com.myfitmate.myfitmate.domain.statistics.dto.NutrientSummaryDto(
        CAST(COALESCE(SUM(mf.food.protein * mf.quantity), 0.0) AS double),
        CAST(COALESCE(SUM(mf.food.fat * mf.quantity), 0.0) AS double),
        CAST(COALESCE(SUM(mf.food.carbohydrate * mf.quantity), 0.0) AS double)
    )
    FROM MealFood mf
    WHERE mf.meal.userId = :userId
      AND DATE(mf.meal.eatTime) = CURRENT_DATE
""")
    Optional<NutrientSummaryDto> findTodayNutrients(@Param("userId") Long userId);
}