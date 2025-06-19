package com.myfitmate.myfitmate.domain.meal.controller;

import com.myfitmate.myfitmate.domain.meal.dto.MealRequestDto;
import com.myfitmate.myfitmate.domain.meal.dto.MealResponseDto;
import com.myfitmate.myfitmate.domain.meal.exception.ErrorCode;
import com.myfitmate.myfitmate.domain.meal.service.MealService;
import com.myfitmate.myfitmate.domain.meal.exception.MealException;
import com.myfitmate.myfitmate.domain.user.entity.User;
import com.myfitmate.myfitmate.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meals")
public class MealController {

    private final MealService mealService;
    private final UserRepository userRepository;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new MealException(ErrorCode.UNAUTHORIZED_ACCESS));
        return user.getId();
    }

    @PostMapping
    public ResponseEntity<MealResponseDto> registerMeal(@RequestBody MealRequestDto dto) {
        Long userId = getCurrentUserId();
        MealResponseDto response = mealService.registerMeal(dto, userId, null);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MealResponseDto>> getAllMeals() {
        Long userId = getCurrentUserId();
        List<MealResponseDto> meals = mealService.getAllMeals(userId);
        return ResponseEntity.ok(meals);
    }

    @GetMapping("/{mealId}")
    public ResponseEntity<MealResponseDto> getMealById(@PathVariable Long mealId) {
        Long userId = getCurrentUserId();
        MealResponseDto response = mealService.getMealById(mealId, userId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/day")
    public ResponseEntity<List<MealResponseDto>> getMealsByDay(@RequestParam("date") String date) {
        Long userId = getCurrentUserId();
        LocalDate parsedDate = LocalDate.parse(date);
        List<MealResponseDto> meals = mealService.getMealsByDay(userId, parsedDate);
        return ResponseEntity.ok(meals);
    }

    @PutMapping("/{mealId}")
    public ResponseEntity<MealResponseDto> updateMeal(@PathVariable Long mealId,
                                                      @RequestBody MealRequestDto dto) {
        Long userId = getCurrentUserId();
        MealResponseDto response = mealService.updateMeal(mealId, userId, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{mealId}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long mealId) {
        Long userId = getCurrentUserId();
        mealService.deleteMealById(mealId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MealResponseDto> registerMeal(
            @RequestPart("dto") MealRequestDto dto,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {

        Long userId = getCurrentUserId();
        MealResponseDto response = mealService.registerMeal(dto, userId, imageFile);
        return ResponseEntity.ok(response);
    }

}
