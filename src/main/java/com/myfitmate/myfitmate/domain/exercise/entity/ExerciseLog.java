package com.myfitmate.myfitmate.domain.exercise.entity;

import com.myfitmate.myfitmate.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "exercise_log")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ExerciseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    private Float durationMinutes;

    private Float kcalBurned;

    @Column(name = "exercised_at")
    private LocalDateTime exerciseAt;

    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
