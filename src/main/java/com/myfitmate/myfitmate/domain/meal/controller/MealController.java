package com.myfitmate.myfitmate.domain.meal.controller;

import com.myfitmate.myfitmate.domain.meal.dto.MealRequestDto;
import com.myfitmate.myfitmate.domain.meal.dto.MealResponseDto;
import com.myfitmate.myfitmate.domain.meal.service.MealService;
import com.myfitmate.myfitmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meals")
public class MealController {

    private final MealService mealService;

    @PostMapping
    public ResponseEntity<MealResponseDto> registerMeal(
            @RequestPart("dto") MealRequestDto dto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(mealService.registerMeal(dto, userDetails.getUser().getId(), imageFile));
    }

    @PutMapping("/{mealId}")
    public ResponseEntity<MealResponseDto> updateMeal(
            @PathVariable Long mealId,
            @RequestPart("dto") MealRequestDto dto,
            @RequestPart(value = "image", required = false) MultipartFile imageFile,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(mealService.updateMeal(mealId, dto, imageFile, userDetails.getUser().getId()));
    }

    @DeleteMapping("/{mealId}")
    public ResponseEntity<Void> deleteMeal(
            @PathVariable Long mealId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        mealService.deleteMeal(mealId, userDetails.getUser().getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<MealResponseDto>> getAllMeals(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(mealService.getAllMeals(userDetails.getUser().getId()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MealResponseDto>> searchMeals(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(mealService.searchMeals(userDetails.getUser().getId(), keyword));
    }
}
