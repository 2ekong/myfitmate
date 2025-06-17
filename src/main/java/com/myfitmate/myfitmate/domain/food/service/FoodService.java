package com.myfitmate.myfitmate.domain.food.service;

import com.myfitmate.myfitmate.domain.food.dto.FoodRequestDto;
import com.myfitmate.myfitmate.domain.food.dto.FoodResponseDto;
import com.myfitmate.myfitmate.domain.food.entity.Food;
import com.myfitmate.myfitmate.domain.food.exception.FoodErrorCode;
import com.myfitmate.myfitmate.domain.food.exception.FoodException;
import com.myfitmate.myfitmate.domain.food.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    public Long registerFood(FoodRequestDto dto) {
        if (foodRepository.existsByName(dto.getName())) {
            throw new FoodException(FoodErrorCode.DUPLICATE_FOOD);
        }
        Food food = Food.builder()
                .name(dto.getName())
                .calories(dto.getCalories())
                .carbohydrate(dto.getCarbohydrate())
                .protein(dto.getProtein())
                .fat(dto.getFat())
                .build();
        return foodRepository.save(food).getId();
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

    public void deleteFood(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new FoodException(FoodErrorCode.FOOD_NOT_FOUND));
        foodRepository.delete(food);
    }
}
