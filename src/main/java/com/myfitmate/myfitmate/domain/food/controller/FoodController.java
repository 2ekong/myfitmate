package com.myfitmate.myfitmate.domain.food.controller;

import com.myfitmate.myfitmate.domain.food.dto.FoodRequestDto;
import com.myfitmate.myfitmate.domain.food.dto.FoodResponseDto;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodService foodService;
    private final FoodCsvService foodCsvService;

    // ✅ CSV 파일을 업로드하여 DB에 저장
    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsv(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        foodCsvService.importFoodDataFromCsv(file, user);
        return ResponseEntity.ok("CSV 데이터가 성공적으로 업로드되었습니다.");
    }

    // ✅ CSV 파일을 업로드하여 DB 저장 없이 미리보기 조회
    @PostMapping("/preview")
    public ResponseEntity<List<Food>> previewCsv(@RequestParam("file") MultipartFile file) {
        List<Food> foods = foodCsvService.readFoodListFromCsv(file);
        return ResponseEntity.ok(foods);
    }

    // ✅ 식품 등록 (DB 저장)
    @PostMapping
    public ResponseEntity<FoodResponseDto> registerFood(
            @RequestBody @Valid FoodRequestDto dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        Food saved = foodService.registerFood(dto, user);
        return ResponseEntity.ok(FoodResponseDto.fromEntity(saved));
    }

    // ✅ keyword 유무와 관계없이 전체 조회
    @GetMapping
    public ResponseEntity<List<FoodResponseDto>> getFoods(
            @RequestParam(value = "keyword", required = false) String keyword) {

        return ResponseEntity.ok(foodService.getFoods(keyword));
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
