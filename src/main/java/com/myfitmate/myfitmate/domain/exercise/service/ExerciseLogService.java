package com.myfitmate.myfitmate.domain.exercise.service;

import com.myfitmate.myfitmate.domain.exercise.dto.ExerciseLogRequestDto;
import com.myfitmate.myfitmate.domain.exercise.dto.ExerciseLogResponseDto;
import com.myfitmate.myfitmate.domain.exercise.dto.ExerciseSummaryDto;
import com.myfitmate.myfitmate.domain.exercise.entity.Exercise;
import com.myfitmate.myfitmate.domain.exercise.entity.ExerciseLog;
import com.myfitmate.myfitmate.domain.exercise.repository.ExerciseLogRepository;
import com.myfitmate.myfitmate.domain.exercise.repository.ExerciseRepository;
import com.myfitmate.myfitmate.domain.user.entity.User;
import com.myfitmate.myfitmate.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
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
                .exerciseAt(dto.getExerciseAt())
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
                .exerciseAt(log.getExerciseAt())
                .build()
        ).toList();
    }

    public ExerciseSummaryDto getDailySummary(Long userId, LocalDate date) {
        List<ExerciseLog> logs = exerciseLogRepository.findByUserIdAndDate(userId, date);

        float totalKcal = 0;
        float totalDuration = 0;

        for (ExerciseLog log : logs) {
            totalKcal += log.getKcalBurned();
            totalDuration += log.getDurationMinutes();
        }

        return new ExerciseSummaryDto(totalKcal, totalDuration, logs.size());
    }


    public void updateLog(Long logId, ExerciseLogRequestDto dto, Long userId) {
        ExerciseLog log = exerciseLogRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("기록 없음"));

        if (!log.getUser().getId().equals(userId)) {
            throw new RuntimeException("본인 기록만 수정 가능");
        }

        Exercise newExercise = exerciseRepository.findById(dto.getExerciseId())
                .orElseThrow(() -> new RuntimeException("운동 정보 없음"));
        log.setExercise(newExercise);

        float weight = log.getUser().getWeightKg();
        float mets = newExercise.getMets();
        float duration = dto.getDurationMinutes();
        float kcal = (duration * mets * 3.5f * weight) / 200f;

        log.setDurationMinutes(duration);
        log.setExerciseAt(dto.getExerciseAt());
        log.setKcalBurned(kcal);
    }

    public void deleteLog(Long logId, Long userId) {
        ExerciseLog log = exerciseLogRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("기록 없음"));

        if (!log.getUser().getId().equals(userId)) {
            throw new RuntimeException("본인 기록만 삭제 가능");
        }

        exerciseLogRepository.delete(log);
    }
}
