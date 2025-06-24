package com.myfitmate.myfitmate.domain.meal.entity;

import com.myfitmate.myfitmate.domain.meal.entity.MealType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MealLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long mealId;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    @Enumerated(EnumType.STRING)
    private MealType mealType;

    private LocalDateTime actionTime;

    public enum ActionType {
        CREATE, UPDATE, DELETE
    }
}
