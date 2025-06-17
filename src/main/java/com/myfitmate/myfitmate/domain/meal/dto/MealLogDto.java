package com.myfitmate.myfitmate.domain.meal.dto;

import com.myfitmate.myfitmate.domain.meal.entity.MealLog;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MealLogDto {
    private MealLog.ActionType action;
    private LocalDateTime actionTime;
    private String snapshot;

    public static MealLogDto fromEntity(MealLog log) {
        return new MealLogDto(
                log.getAction(),
                log.getActionTime(),
                log.getSnapshot()
        );
    }
}
