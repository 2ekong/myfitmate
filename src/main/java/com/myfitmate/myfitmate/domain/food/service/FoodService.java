package com.myfitmate.myfitmate.domain.food.service;

import com.myfitmate.myfitmate.domain.food.dto.FoodRequestDto;
import com.myfitmate.myfitmate.domain.food.dto.FoodResponseDto;
import com.myfitmate.myfitmate.domain.food.entity.Food;
import com.myfitmate.myfitmate.domain.food.exception.FoodErrorCode;
import com.myfitmate.myfitmate.domain.food.exception.FoodException;
import com.myfitmate.myfitmate.domain.food.repository.FoodRepository;
import com.myfitmate.myfitmate.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    // 음식 등록은 로그인 사용자만 가능
    public Food registerFood(FoodRequestDto dto, User user) {
        if (foodRepository.existsByName(dto.getName())) {
            throw new FoodException(FoodErrorCode.DUPLICATE_FOOD);
        }

        Food food = Food.builder()
                .name(dto.getName())
                .standardAmount(dto.getStandardAmount())
                .calories(dto.getCalories())
                .carbohydrate(dto.getCarbohydrate())
                .protein(dto.getProtein())
                .fat(dto.getFat())
                .sodium(dto.getSodium())
                .referenceBasis(dto.getReferenceBasis())
                .user(user)
                .build();

        return foodRepository.save(food);
    }

    // ✅ 모든 사용자가 조회할 수 있는 전체 음식 목록
    public List<FoodResponseDto> getAllFoods() {
        return foodRepository.findAll().stream()
                .map(FoodResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // ✅ 특정 ID로 음식 조회 (누구나 가능)
    public FoodResponseDto getFoodById(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new FoodException(FoodErrorCode.FOOD_NOT_FOUND));

        return FoodResponseDto.fromEntity(food);
    }

    // 음식 삭제는 로그인 사용자만 가능
    public void deleteFood(Long id, User user) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new FoodException(FoodErrorCode.FOOD_NOT_FOUND));

        if (!food.getUser().getId().equals(user.getId())) {
            throw new FoodException(FoodErrorCode.UNAUTHORIZED_ACCESS);
        }

        foodRepository.delete(food);
    }

    // ✅ 공용 키워드 검색
    public List<FoodResponseDto> searchFoods(String keyword) {
        return foodRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(FoodResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // ✅ 공용 조건 검색 (키워드 없으면 전체 반환)
    public List<FoodResponseDto> getFoods(String keyword) {
        List<Food> foods;

        if (keyword == null || keyword.isBlank()) {
            foods = foodRepository.findAll();
        } else {
            foods = foodRepository.findByNameContainingIgnoreCase(keyword);
        }

        return foods.stream()
                .map(FoodResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}
