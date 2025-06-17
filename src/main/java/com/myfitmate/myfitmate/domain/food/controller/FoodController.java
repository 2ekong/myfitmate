package com.myfitmate.myfitmate.domain.food.controller;

import com.myfitmate.myfitmate.domain.food.dto.FoodRequestDto;
import com.myfitmate.myfitmate.domain.food.dto.FoodResponseDto;
import com.myfitmate.myfitmate.domain.food.service.FoodService;
import com.myfitmate.myfitmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodService foodService;

    @PostMapping
    public Long registerFood(@RequestBody FoodRequestDto requestDto,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return foodService.registerFood(requestDto);
    }

    @GetMapping
    public List<FoodResponseDto> getAllFoods() {
        return foodService.getAllFoods();
    }

    @GetMapping("/{id}")
    public FoodResponseDto getFoodById(@PathVariable Long id) {
        return foodService.getFoodById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
    }
}
