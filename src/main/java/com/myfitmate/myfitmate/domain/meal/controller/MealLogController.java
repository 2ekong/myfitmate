package com.myfitmate.myfitmate.domain.meal.controller;

import com.myfitmate.myfitmate.domain.meal.dto.MealLogDto;
import com.myfitmate.myfitmate.domain.meal.entity.MealLog;
import com.myfitmate.myfitmate.domain.meal.repository.MealLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meals/logs")
public class MealLogController {

    private final MealLogRepository mealLogRepository;

    @GetMapping
    public ResponseEntity<List<MealLogDto>> getAllLogs() {
        List<MealLog> logs = mealLogRepository.findAll();

        List<MealLogDto> result = logs.stream().map(log ->
                new MealLogDto(
                        log.getId(),
                        log.getMealId(),
                        log.getUserId(),
                        log.getActionType(),
                        log.getActionTime()
                )
        ).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
