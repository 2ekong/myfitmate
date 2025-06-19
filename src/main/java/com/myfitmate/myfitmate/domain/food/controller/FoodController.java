package com.myfitmate.myfitmate.domain.food.controller;

import com.myfitmate.myfitmate.domain.food.dto.FoodRequestDto;
import com.myfitmate.myfitmate.domain.food.dto.FoodResponseDto;
import com.myfitmate.myfitmate.domain.food.entity.Food;
import com.myfitmate.myfitmate.domain.food.service.FoodService;
import com.myfitmate.myfitmate.domain.food.service.FoodOpenApiService;
import com.myfitmate.myfitmate.security.UserDetailsImpl;
import com.myfitmate.myfitmate.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodService foodService;
    private final FoodOpenApiService foodOpenApiService;

    @PostMapping
    public FoodResponseDto registerFood(@RequestBody @Valid FoodRequestDto dto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        Food savedFood = foodService.registerFood(dto, user);
        return FoodResponseDto.fromEntity(savedFood);
    }


    @GetMapping
    public List<FoodResponseDto> getAllFoods(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return foodService.getAllFoods(user);
    }

    @GetMapping("/{id}")
    public FoodResponseDto getFoodById(@PathVariable Long id,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        return foodService.getFoodById(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteFood(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        foodService.deleteFood(id, user);
    }

    @PostMapping("/fetch")
    public ResponseEntity<String> fetchFoodData() {
        foodOpenApiService.fetchAndSave();
        return ResponseEntity.ok("공공 데이터 연동 완료");
    }


    @GetMapping("/search")
    public List<FoodResponseDto> searchFood(@RequestParam String keyword,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return foodService.searchFoods(keyword, userDetails.getUser());
    }


}
