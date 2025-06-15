package com.myfitmate.myfitmate.domain.exercise.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ExerciseLogResponseDto {
    private String exerciseName;
    private float durationMinutes;
    private float kcalBurned;
    private LocalDateTime exerciseAt;
}
