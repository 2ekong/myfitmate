package com.myfitmate.myfitmate.domain.meal.controller;

import com.myfitmate.myfitmate.domain.meal.dto.MealLogDto;
import com.myfitmate.myfitmate.domain.meal.entity.Meal;
import com.myfitmate.myfitmate.domain.meal.repository.MealLogRepository;
import com.myfitmate.myfitmate.domain.meal.service.MealService;
import com.myfitmate.myfitmate.domain.meal.dto.MealRequestDto;
import com.myfitmate.myfitmate.domain.meal.dto.MealResponseDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;
    private final MealLogRepository mealLogRepository;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MealResponseDto> registerMeal(
            @RequestPart("dto") @Valid MealRequestDto dto,
            @RequestPart(value = "image", required = false) MultipartFile image,
            HttpServletRequest request
    ) {
        Long userId = extractUserId(request);
        MealResponseDto response = mealService.registerMeal(dto, userId, image);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/day")
    public ResponseEntity<List<MealResponseDto>> getMealsByDay(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            HttpServletRequest request
    ) {
        Long userId = extractUserId(request);
        List<MealResponseDto> meals = mealService.getMealsByDay(userId, date);
        return ResponseEntity.ok(meals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealResponseDto> getMealById(@PathVariable Long id, HttpServletRequest request) {
        Long userId = extractUserId(request);
        Meal meal = mealService.getMealById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 식단이 존재하지 않습니다."));

        if (!meal.getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인의 식단만 조회할 수 있습니다.");
        }

        MealResponseDto dto = mealService.convertToDto(meal);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long id, HttpServletRequest request) {
        Long userId = extractUserId(request);
        mealService.deleteMealById(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{mealId}/logs")
    public ResponseEntity<List<MealLogDto>> getMealLogs(@PathVariable Long mealId) {
        List<MealLogDto> logs = mealLogRepository.findByMealIdOrderByActionTimeDesc(mealId).stream()
                .map(MealLogDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(logs);
    }

    private Long extractUserId(HttpServletRequest request) {
        Object userIdAttr = request.getAttribute("userId");
        if (userIdAttr == null || !(userIdAttr instanceof Long)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        return (Long) userIdAttr;
    }
}
