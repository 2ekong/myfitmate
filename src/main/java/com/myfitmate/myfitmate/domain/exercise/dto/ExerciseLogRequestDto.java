package com.myfitmate.myfitmate.domain.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ExerciseLogRequestDto {
    private Long exerciseId;
    private Float durationMinutes;
    private LocalDateTime exercisedAt;
}
