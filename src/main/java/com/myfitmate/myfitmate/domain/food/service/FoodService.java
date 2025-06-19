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

    public Long registerFood(FoodRequestDto dto, User user) {
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

        return foodRepository.save(food).getId();
    }

    public List<FoodResponseDto> getAllFoods(User user) {
        return foodRepository.findByUser(user).stream()
                .map(FoodResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public FoodResponseDto getFoodById(Long id, User user) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new FoodException(FoodErrorCode.FOOD_NOT_FOUND));

        if (!food.getUser().getId().equals(user.getId())) {
            throw new FoodException(FoodErrorCode.UNAUTHORIZED_ACCESS);
        }

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

    public List<FoodResponseDto> searchFoods(String keyword, User user) {
        List<Food> foods = foodRepository.findByNameContaining(keyword);
        return foods.stream()
                .filter(f -> f.getUser().getId().equals(user.getId()))
                .map(FoodResponseDto::fromEntity)
                .toList();
    }

}
