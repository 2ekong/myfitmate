package com.myfitmate.myfitmate.domain.food.controller;

import com.myfitmate.myfitmate.domain.food.dto.FoodRequestDto;
import com.myfitmate.myfitmate.domain.food.dto.FoodResponseDto;
import com.myfitmate.myfitmate.domain.food.dto.FoodCsvDto;
import com.myfitmate.myfitmate.domain.food.entity.Food;
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

    // ✅ CSV 기반 음식 검색 (비로그인 공개)
    @GetMapping("/search")
    public ResponseEntity<List<FoodCsvDto>> searchCsvFoods(
            @RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(foodCsvService.searchFoods(keyword));
    }

    // ✅ CSV 전체 목록 조회 (비로그인 공개)
    @GetMapping("/csv")
    public ResponseEntity<List<FoodCsvDto>> getAllCsvFoods() {
        return ResponseEntity.ok(foodCsvService.getAllFoods());
    }

    // ✅ 식품 등록 (DB 저장, 로그인 필요)
    @PostMapping
    public ResponseEntity<FoodResponseDto> registerFood(
            @RequestBody @Valid FoodRequestDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        Food saved = foodService.registerFood(dto, user);
        return ResponseEntity.ok(FoodResponseDto.fromEntity(saved));
    }

    // ✅ keyword 유무와 관계없이 사용자 음식 전체 조회
    @GetMapping
    public ResponseEntity<List<FoodResponseDto>> getFoods(
            @RequestParam(value = "keyword", required = false) String keyword,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(foodService.getFoods(keyword, userDetails.getUser()));
    }


    // ✅ ID로 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<FoodResponseDto> getFoodById(@PathVariable Long id) {
        return ResponseEntity.ok(foodService.getFoodById(id));
    }

    // ✅ 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        foodService.deleteFood(id, user);
        return ResponseEntity.noContent().build();
    }
}
