package com.myfitmate.myfitmate.domain.exercise.service;

import com.myfitmate.myfitmate.domain.exercise.dto.ExerciseLogRequestDto;
import com.myfitmate.myfitmate.domain.exercise.dto.ExerciseLogResponseDto;
import com.myfitmate.myfitmate.domain.exercise.entity.Exercise;
import com.myfitmate.myfitmate.domain.exercise.entity.ExerciseLog;
import com.myfitmate.myfitmate.domain.exercise.repository.ExerciseLogRepository;
import com.myfitmate.myfitmate.domain.exercise.repository.ExerciseRepository;
import com.myfitmate.myfitmate.domain.user.entity.User;
import com.myfitmate.myfitmate.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseLogService {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseLogRepository exerciseLogRepository;
    private final UserRepository userRepository;

    public void logExercise(ExerciseLogRequestDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        Exercise exercise = exerciseRepository.findById(dto.getExerciseId())
                .orElseThrow(() -> new RuntimeException("운동 정보 없음"));

        float weight = user.getWeightKg();
        float mets = exercise.getMets();
        float duration = dto.getDurationMinutes();

        float kcal = (duration * mets * 3.5f * weight) / 200f;

        ExerciseLog log = ExerciseLog.builder()
                .user(user)
                .exercise(exercise)
                .durationMinutes(duration)
                .kcalBurned(kcal)
                .exerciseAt(dto.getExercisedAt())
                .build();

        exerciseLogRepository.save(log);
    }

    public List<ExerciseLogResponseDto> getLogsByUser(Long userId, LocalDate date) {
        List<ExerciseLog> logs;

        if (date != null) {
            logs = exerciseLogRepository.findByUserIdAndDate(userId, date);
        } else {
            logs = exerciseLogRepository.findByUserId(userId);
        }

        return logs.stream().map(log -> ExerciseLogResponseDto.builder()
                .exerciseName(log.getExercise().getName())
                .durationMinutes(log.getDurationMinutes())
                .kcalBurned(log.getKcalBurned())
                .exercisedAt(log.getExerciseAt())
                .build()
        ).toList();
    }


}
