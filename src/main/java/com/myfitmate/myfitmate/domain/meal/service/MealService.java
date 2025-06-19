package com.myfitmate.myfitmate.domain.meal.service;

import com.myfitmate.myfitmate.domain.food.entity.Food;
import com.myfitmate.myfitmate.domain.food.repository.FoodRepository;
import com.myfitmate.myfitmate.domain.meal.dto.MealRequestDto;
import com.myfitmate.myfitmate.domain.meal.dto.MealResponseDto;
import com.myfitmate.myfitmate.domain.meal.dto.MealResponseDto.FoodDetail;
import com.myfitmate.myfitmate.domain.meal.entity.Meal;
import com.myfitmate.myfitmate.domain.meal.entity.MealFood;
import com.myfitmate.myfitmate.domain.meal.entity.MealImage;
import com.myfitmate.myfitmate.domain.meal.entity.MealLog;
import com.myfitmate.myfitmate.domain.meal.exception.ErrorCode;
import com.myfitmate.myfitmate.domain.meal.exception.MealException;
import com.myfitmate.myfitmate.domain.meal.repository.MealFoodRepository;
import com.myfitmate.myfitmate.domain.meal.repository.MealImageRepository;
import com.myfitmate.myfitmate.domain.meal.repository.MealLogRepository;
import com.myfitmate.myfitmate.domain.meal.repository.MealRepository;
import com.myfitmate.myfitmate.domain.meal.util.FileStorageUtil;
import com.myfitmate.myfitmate.domain.user.entity.User;
import com.myfitmate.myfitmate.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MealService {

    private final MealRepository mealRepository;
    private final MealFoodRepository mealFoodRepository;
    private final FoodRepository foodRepository;
    private final UserRepository userRepository;
    private final MealLogRepository mealLogRepository;
    private final MealImageRepository mealImageRepository;

    public MealResponseDto registerMeal(MealRequestDto dto, Long userId, MultipartFile imageFile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new MealException(ErrorCode.UNAUTHORIZED_ACCESS));

        LocalDateTime eatTime = dto.getEatTime();
        LocalDateTime startOfDay = eatTime.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = eatTime.toLocalDate().atTime(LocalTime.MAX);

        boolean exists = mealRepository.existsByUserIdAndEatTimeBetweenAndMealType(
                userId, startOfDay, endOfDay, dto.getMealType());

        if (exists) {
            throw new MealException(ErrorCode.DUPLICATE_MEAL);
        }

        Meal meal = Meal.builder()
                .userId(userId)
                .eatTime(dto.getEatTime())
                .mealType(dto.getMealType())
                .totalCalories(0f)
                .hasImage(false)
                .build();

        Meal savedMeal = mealRepository.save(meal);

        float totalCalories = 0f;
        for (MealRequestDto.FoodItem item : dto.getFoodList()) {
            Food food = foodRepository.findById(item.foodId())
                    .orElseThrow(() -> new MealException(ErrorCode.FOOD_NOT_FOUND));

            Float std = food.getStandardAmount();
            if (std == null || std == 0f) {
                throw new MealException(ErrorCode.INVALID_FOOD_STANDARD);
            }

            float cal = food.getCalories() * (item.quantity() / std);
            totalCalories += cal;

            MealFood mealFood = MealFood.builder()
                    .meal(savedMeal)
                    .foodId(item.foodId())
                    .quantity(item.quantity())
                    .calories(cal)
                    .build();

            mealFoodRepository.save(mealFood);
        }

        savedMeal.setTotalCalories(totalCalories);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imagePath = FileStorageUtil.saveImage(imageFile, "meal-images");
                String hash = FileStorageUtil.generateFileHash(imageFile);

                MealImage mealImage = MealImage.builder()
                        .meal(savedMeal)
                        .filePath(imagePath)
                        .hash(hash)
                        .build();

                mealImageRepository.save(mealImage);
                savedMeal.setHasImage(true);
            } catch (IOException e) {
                throw new MealException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        return convertToDto(savedMeal);
    }

    public List<MealResponseDto> getAllMeals(Long userId) {
        List<Meal> meals = mealRepository.findByUserIdOrderByEatTimeDesc(userId);
        return meals.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MealResponseDto convertToDto(Meal meal) {
        List<MealFood> mealFoods = mealFoodRepository.findByMealId(meal.getId());
        List<FoodDetail> foodList = mealFoods.stream().map(mf -> {
            String name = foodRepository.findById(mf.getFoodId())
                    .map(Food::getName)
                    .orElse("unknown");
            return new FoodDetail(mf.getFoodId(), name, mf.getQuantity(), mf.getCalories());
        }).collect(Collectors.toList());

        return new MealResponseDto(
                meal.getId(),
                meal.getEatTime(),
                meal.getMealType(),
                meal.getTotalCalories(),
                foodList
        );
    }

    public MealResponseDto getMealById(Long mealId, Long userId) {
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new MealException(ErrorCode.MEAL_NOT_FOUND));

        if (!userId.equals(meal.getUserId().longValue())) {
            throw new MealException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        return convertToDto(meal);
    }

    public List<MealResponseDto> getMealsByDay(Long userId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        return mealRepository.findByUserIdAndEatTimeBetweenOrderByEatTimeAsc(userId, start, end)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void deleteMealById(Long id, Long userId) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new MealException(ErrorCode.MEAL_NOT_FOUND));

        if (!meal.getUserId().equals(userId)) {
            throw new MealException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        mealFoodRepository.deleteByMealId(id);
        mealRepository.delete(meal);
    }

    public MealResponseDto updateMeal(Long mealId, Long userId, MealRequestDto dto) {
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new MealException(ErrorCode.MEAL_NOT_FOUND));

        if (!meal.getUserId().equals(userId)) {
            throw new MealException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        meal.setEatTime(dto.getEatTime());
        meal.setMealType(dto.getMealType());
        mealFoodRepository.deleteByMealId(mealId);

        float totalCalories = 0f;
        for (MealRequestDto.FoodItem item : dto.getFoodList()) {
            Food food = foodRepository.findById(item.foodId())
                    .orElseThrow(() -> new MealException(ErrorCode.FOOD_NOT_FOUND));
            Float std = food.getStandardAmount();
            if (std == null || std == 0f) throw new MealException(ErrorCode.INVALID_FOOD_STANDARD);

            float cal = food.getCalories() * (item.quantity() / std);
            totalCalories += cal;

            MealFood mealFood = MealFood.builder()
                    .meal(meal)
                    .foodId(item.foodId())
                    .quantity(item.quantity())
                    .calories(cal)
                    .build();
            mealFoodRepository.save(mealFood);
        }

        meal.setTotalCalories(totalCalories);
        return convertToDto(meal);
    }

    private void saveMealLog(Meal meal, Long userId, MealLog.ActionType actionType) {
        MealLog log = MealLog.builder()
                .mealId(meal.getId())
                .userId(userId)
                .actionType(actionType)
                .mealType(meal.getMealType())
                .actionTime(LocalDateTime.now())
                .build();
        mealLogRepository.save(log);
    }
}
