package com.myfitmate.myfitmate.domain.meal.dto;

import com.myfitmate.myfitmate.domain.meal.entity.MealLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MealLogDto {
    private Long id;
    private Long mealId;
    private Long userId;
    private MealLog.ActionType actionType;
    private LocalDateTime actionTime;
}
