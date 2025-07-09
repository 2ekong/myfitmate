package com.myfitmate.myfitmate.domain.statistics.dto;

import java.util.List;

public record WeeklyCaloriesResponseDto(
        List<String> labels,
        List<Float> mealCalories,
        List<Float> exerciseCalories
) {}