package com.myfitmate.myfitmate.domain.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyCalorieResponse {
    private LocalDate date;
    private Float mealCalories;
    private Float exerciseCalories;
}