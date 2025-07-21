package com.myfitmate.myfitmate.domain.exercise.repository;

import com.myfitmate.myfitmate.domain.exercise.entity.ExerciseLog;
import com.myfitmate.myfitmate.domain.statistics.dto.DailyCalorieDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {
    // 전체 기록 조회
    List<ExerciseLog> findByUserId(Long userId);

    // 날짜별 조회 (DATE 함수 사용)
    @Query("SELECT l FROM ExerciseLog l WHERE l.user.id = :userId AND DATE(l.exerciseAt) = :date")
    List<ExerciseLog> findByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    @Query("""
SELECT new com.myfitmate.myfitmate.domain.statistics.dto.DailyCalorieDto(
    DATE(e.exerciseAt), SUM(e.kcalBurned)
)
FROM ExerciseLog e
WHERE e.user.id = :userId AND e.exerciseAt >= :startDate
GROUP BY DATE(e.exerciseAt)
""")
    List<DailyCalorieDto> findDailyExerciseCalories(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate);

}
