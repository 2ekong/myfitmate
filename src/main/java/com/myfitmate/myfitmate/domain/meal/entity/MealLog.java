package com.myfitmate.myfitmate.domain.meal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "meal_log")
public class MealLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long mealId;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private ActionType action;

    @Lob
    private String snapshot;

    @Column(name = "action_time", columnDefinition = "datetime default current_timestamp")
    private LocalDateTime actionTime;

    public enum ActionType {
        CREATE, UPDATE, DELETE
    }
}
