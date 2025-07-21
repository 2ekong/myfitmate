package com.myfitmate.myfitmate.domain.food.service;

import com.myfitmate.myfitmate.domain.food.dto.FoodRequestDto;
import com.myfitmate.myfitmate.domain.food.entity.Food;
import com.myfitmate.myfitmate.domain.food.exception.FoodErrorCode;
import com.myfitmate.myfitmate.domain.food.exception.FoodException;
import com.myfitmate.myfitmate.domain.food.repository.FoodRepository;
import com.myfitmate.myfitmate.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    @Transactional
    public Food registerFood(FoodRequestDto dto, User user) {
        if (foodRepository.existsByNameAndUser(dto.getName(), user)) {
            throw new FoodException(FoodErrorCode.DUPLICATE_FOOD);
        }
        Food food = dto.toEntity(user);
        food.setUser(user);
        return foodRepository.save(food);
    }

    @Transactional(readOnly = true)
    public List<Food> getFoods(String keyword, User user) {
        if (keyword == null || keyword.isBlank()) {
            return foodRepository.findAllByUser(user);
        }
        return foodRepository.findByNameContainingIgnoreCaseAndUser(keyword, user);
    }

    @Transactional(readOnly = true)
    public Food getFoodById(Long id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new FoodException(FoodErrorCode.FOOD_NOT_FOUND));
    }

    @Transactional
    public Food updateFood(Long id, FoodRequestDto dto, User user) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new FoodException(FoodErrorCode.FOOD_NOT_FOUND));
        if (!food.getUser().getId().equals(user.getId())) {
            throw new FoodException(FoodErrorCode.UNAUTHORIZED_ACCESS);
        }
        food.updateFromDto(dto);
        return food;
    }

    @Transactional
    public void deleteFood(Long id, User user) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new FoodException(FoodErrorCode.FOOD_NOT_FOUND));
        if (!food.getUser().getId().equals(user.getId())) {
            throw new FoodException(FoodErrorCode.UNAUTHORIZED_ACCESS);
        }
        foodRepository.delete(food);
    }
}
