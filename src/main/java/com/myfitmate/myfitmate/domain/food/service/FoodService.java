package com.myfitmate.myfitmate.domain.food.service;

import com.myfitmate.myfitmate.domain.food.dto.FoodRequestDto;
import com.myfitmate.myfitmate.domain.food.dto.FoodResponseDto;
import com.myfitmate.myfitmate.domain.food.entity.Food;
import com.myfitmate.myfitmate.domain.food.exception.FoodErrorCode;
import com.myfitmate.myfitmate.domain.food.exception.FoodException;
import com.myfitmate.myfitmate.domain.food.repository.FoodRepository;
import com.myfitmate.myfitmate.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FoodService {

    private final FoodRepository foodRepository;

    public Food registerFood(FoodRequestDto dto, User user) {
        if (foodRepository.existsByName(dto.getName())) {
            throw new FoodException(FoodErrorCode.DUPLICATE_FOOD);
        }

        log.info("===> name: {}", dto.getName());
        log.info("===> standardAmount: {}", dto.getStandardAmount());
        log.info("===> calories: {}", dto.getCalories());
        log.info("===> protein: {}", dto.getProtein());
        log.info("===> fat: {}", dto.getFat());
        log.info("===> carbohydrate: {}", dto.getCarbohydrate());
        log.info("===> sodium: {}", dto.getSodium());
        log.info("===> referenceBasis: {}", dto.getReferenceBasis());
        log.info("===> originCategory: {}", dto.getOriginCategory());
        log.info("===> originSubCategory: {}", dto.getOriginSubCategory());
        log.info("===> originDetailCategory: {}", dto.getOriginDetailCategory());

        Food food = Food.builder()
                .name(dto.getName())
                .originCategory(dto.getOriginCategory())
                .originSubCategory(dto.getOriginSubCategory())
                .originDetailCategory(dto.getOriginDetailCategory())
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

    public List<FoodResponseDto> getAllFoods() {
        return foodRepository.findAll().stream()
                .map(FoodResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public FoodResponseDto getFoodById(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new FoodException(FoodErrorCode.FOOD_NOT_FOUND));
        return FoodResponseDto.fromEntity(food);
    }

    public void deleteFood(Long id, User user) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new FoodException(FoodErrorCode.FOOD_NOT_FOUND));
        if (!food.getUser().getId().equals(user.getId())) {
            throw new FoodException(FoodErrorCode.UNAUTHORIZED_ACCESS);
        }
        foodRepository.delete(food);
    }

    public List<FoodResponseDto> searchFoods(String keyword) {
        return foodRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(FoodResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<FoodResponseDto> getFoods(String keyword, User user) {
        List<Food> foods;

        if (keyword == null || keyword.isBlank()) {
            foods = foodRepository.findByUser(user);
        } else {
            foods = foodRepository.findByUserAndNameContainingIgnoreCase(user, keyword);
        }

        return foods.stream()
                .map(FoodResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

}
