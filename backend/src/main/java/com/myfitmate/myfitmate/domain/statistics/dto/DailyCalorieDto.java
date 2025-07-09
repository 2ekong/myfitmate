package com.myfitmate.myfitmate.domain.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class DailyCalorieDto {

    private LocalDate date;
    private Float calories;

    // 꼭 있어야 하는 생성자 (JPQL에서 new DailyCalorieDto(...) 할 때 호출됨)
    public DailyCalorieDto(java.sql.Date date, Double calories) {
        this.date = date.toLocalDate();
        this.calories = calories != null ? calories.floatValue() : 0f;
    }
}
