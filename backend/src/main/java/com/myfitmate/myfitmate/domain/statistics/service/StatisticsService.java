package com.myfitmate.myfitmate.domain.statistics.service;

import com.myfitmate.myfitmate.domain.exercise.repository.ExerciseLogRepository;
import com.myfitmate.myfitmate.domain.meal.repository.MealRepository;
import com.myfitmate.myfitmate.domain.statistics.dto.NutrientSummaryDto;
import com.myfitmate.myfitmate.domain.statistics.dto.WeeklyCaloriesResponseDto;
import com.myfitmate.myfitmate.domain.statistics.dto.DailyCalorieDto;
import com.myfitmate.myfitmate.domain.statistics.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final MealRepository mealRepository;
    private final ExerciseLogRepository exerciseLogRepository;
    private final StatisticsRepository statisticsRepository;

    public WeeklyCaloriesResponseDto getWeeklyCalories(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(6);
        LocalDateTime startDateTime = startDate.atStartOfDay(); // meal용

        Map<LocalDate, Float> mealMap = mealRepository
                .findDailyMealCalories(userId, startDateTime)
                .stream()
                .collect(Collectors.toMap(DailyCalorieDto::getDate, DailyCalorieDto::getCalories));

        Map<LocalDate, Float> exerciseMap = exerciseLogRepository
                .findDailyExerciseCalories(userId, startDateTime) // exercise는 LocalDate
                .stream()
                .collect(Collectors.toMap(DailyCalorieDto::getDate, DailyCalorieDto::getCalories));

        // 차트용 라벨과 값 리스트
        List<String> labels = new ArrayList<>();
        List<Float> mealCalories = new ArrayList<>();
        List<Float> exerciseCalories = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            labels.add(date.toString());
            mealCalories.add(mealMap.getOrDefault(date, 0f));
            exerciseCalories.add(exerciseMap.getOrDefault(date, 0f));
        }

        return new WeeklyCaloriesResponseDto(labels, mealCalories, exerciseCalories);
    }

    public NutrientSummaryDto getTodayNutrients(Long userId) {
        return statisticsRepository.findTodayNutrients(userId)
                .orElse(new NutrientSummaryDto(0.0, 0.0, 0.0));
    }
}
