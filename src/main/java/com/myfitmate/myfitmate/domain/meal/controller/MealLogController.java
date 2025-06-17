package com.myfitmate.myfitmate.domain.meal.controller;

import com.myfitmate.myfitmate.domain.meal.repository.MealLogRepository;
import com.myfitmate.myfitmate.domain.meal.dto.MealLogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meals")
public class MealLogController {

    private final MealLogRepository mealLogRepository;

    @GetMapping("/{mealId}/logs")
    public ResponseEntity<List<MealLogDto>> getMealLogs(@PathVariable Long mealId) {
        List<MealLogDto> logs = mealLogRepository.findByMealIdOrderByActionTimeDesc(mealId).stream()
                .map(MealLogDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(logs);
    }
}
