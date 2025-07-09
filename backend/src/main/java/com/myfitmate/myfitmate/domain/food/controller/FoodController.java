package com.myfitmate.myfitmate.domain.food.controller;

import com.myfitmate.myfitmate.domain.food.dto.FoodRequestDto;
import com.myfitmate.myfitmate.domain.food.dto.FoodResponseDto;
import com.myfitmate.myfitmate.domain.food.dto.FoodCsvDto;
import com.myfitmate.myfitmate.domain.food.entity.Food;
import com.myfitmate.myfitmate.domain.food.exception.FoodErrorCode;
import com.myfitmate.myfitmate.domain.food.exception.FoodException;
import com.myfitmate.myfitmate.domain.food.service.FoodCsvService;
import com.myfitmate.myfitmate.domain.food.service.FoodService;
import com.myfitmate.myfitmate.security.UserDetailsImpl;
import com.myfitmate.myfitmate.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodService foodService;
    private final FoodCsvService foodCsvService;

    @GetMapping("/search")
    public ResponseEntity<List<FoodCsvDto>> searchCsvFoods(
            @RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(foodCsvService.searchFoods(keyword));
    }

    @GetMapping("/csv")
    public ResponseEntity<List<FoodCsvDto>> getAllCsvFoods() {
        return ResponseEntity.ok(foodCsvService.getAllFoods());
    }

    @PostMapping
    public ResponseEntity<FoodResponseDto> registerFood(
            @RequestBody @Valid FoodRequestDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        Food saved = foodService.registerFood(dto, user);
        return ResponseEntity.ok(FoodResponseDto.fromEntity(saved));
    }

    @GetMapping
    public ResponseEntity<List<FoodResponseDto>> getFoods(
            @RequestParam(value = "keyword", required = false) String keyword,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok(
                foodService.getFoods(keyword, userDetails.getUser())
                        .stream()
                        .map(FoodResponseDto::fromEntity)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodResponseDto> getFoodById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Food food = foodService.getFoodById(id);

        // ✅ 사용자 소유 검사
        if (!food.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new FoodException(FoodErrorCode.UNAUTHORIZED_ACCESS);
        }

        return ResponseEntity.ok(FoodResponseDto.fromEntity(food));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        foodService.deleteFood(id, user);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodResponseDto> updateFood(
            @PathVariable Long id,
            @RequestBody @Valid FoodRequestDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        Food updated = foodService.updateFood(id, dto, user);
        return ResponseEntity.ok(FoodResponseDto.fromEntity(updated));
    }
}
