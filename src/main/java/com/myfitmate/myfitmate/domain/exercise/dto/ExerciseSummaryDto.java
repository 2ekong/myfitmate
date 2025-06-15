package com.myfitmate.myfitmate.domain.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExerciseSummaryDto {
        private float totalKcal;
        private float totalDuration;
        private int exerciseCount;
}
