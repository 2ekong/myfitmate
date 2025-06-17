package com.myfitmate.myfitmate.domain.meal.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myfitmate.myfitmate.domain.food.entity.Food;
import com.myfitmate.myfitmate.domain.food.repository.FoodRepository;
import com.myfitmate.myfitmate.domain.meal.dto.MealRequestDto;
import com.myfitmate.myfitmate.domain.meal.dto.MealResponseDto;
import com.myfitmate.myfitmate.domain.meal.dto.MealResponseDto.FoodDetail;
import com.myfitmate.myfitmate.domain.meal.entity.Meal;
import com.myfitmate.myfitmate.domain.meal.entity.MealFood;
import com.myfitmate.myfitmate.domain.meal.entity.MealImage;
import com.myfitmate.myfitmate.domain.meal.entity.MealLog;
import com.myfitmate.myfitmate.domain.meal.exception.MealErrorCode;
import com.myfitmate.myfitmate.domain.meal.exception.MealException;
import com.myfitmate.myfitmate.domain.meal.repository.MealFoodRepository;
import com.myfitmate.myfitmate.domain.meal.repository.MealImageRepository;
import com.myfitmate.myfitmate.domain.meal.repository.MealLogRepository;
import com.myfitmate.myfitmate.domain.meal.repository.MealRepository;
import com.myfitmate.myfitmate.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MealService {

    private final MealRepository mealRepository;
    private final FoodRepository foodRepository;
    private final UserRepository userRepository;
    private final MealFoodRepository mealFoodRepository;
    private final MealImageRepository mealImageRepository;
    private final MealLogRepository mealLogRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MealResponseDto registerMeal(MealRequestDto dto, Long userId, MultipartFile imageFile) {
        userRepository.findById(userId)
                .orElseThrow(() -> new MealException(MealErrorCode.USER_NOT_FOUND));

        LocalDate date = dto.getEatTime().toLocalDate();
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        boolean exists = mealRepository.existsByUserIdAndEatTimeBetweenAndMealType(
                userId, startOfDay, endOfDay, dto.getMealType());

        if (exists) {
            throw new MealException(MealErrorCode.DUPLICATE_MEAL);
        }

        Meal meal = Meal.builder()
                .userId(userId)
                .eatTime(dto.getEatTime())
                .mealType(dto.getMealType())
                .hasImage(imageFile != null && !imageFile.isEmpty())
                .totalCalories(0f)
                .build();

        float totalCalories = 0f;

        for (MealRequestDto.FoodItem item : dto.getFoodList()) {
            Food food = foodRepository.findById(item.foodId())
                    .orElseThrow(() -> new MealException(MealErrorCode.FOOD_NOT_FOUND));

            float calories = food.getCalories() * (item.quantity() / food.getStandardAmount());
            totalCalories += calories;
            meal.addMealFood(item.foodId(), item.quantity(), calories);
        }

        meal.setTotalCalories(totalCalories);
        Meal savedMeal = mealRepository.save(meal);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String uploadDir = "src/main/resources/static/uploads/meal-images/";
                String fileName = "meal_" + savedMeal.getId() + "_" + System.currentTimeMillis() + ".jpg";
                Path filePath = Paths.get(uploadDir + fileName);

                File dir = new File(uploadDir);
                if (!dir.exists() && !dir.mkdirs()) {
                    throw new MealException(MealErrorCode.IMAGE_DIRECTORY_CREATE_FAIL);
                }

                Files.write(filePath, imageFile.getBytes());
                String imageUrl = "/uploads/meal-images/" + fileName;

                MealImage mealImage = MealImage.builder()
                        .mealId(savedMeal.getId())
                        .filePath(imageUrl)
                        .hash(String.valueOf(imageFile.hashCode()))
                        .build();
                mealImageRepository.save(mealImage);

            } catch (IOException e) {
                throw new MealException(MealErrorCode.IMAGE_SAVE_FAIL, e);
            }
        }

        try {
            MealLog mealLog = MealLog.builder()
                    .mealId(savedMeal.getId())
                    .userId(userId)
                    .action(MealLog.ActionType.CREATE)
                    .snapshot(objectMapper.writeValueAsString(savedMeal))
                    .actionTime(LocalDateTime.now())
                    .build();
            mealLogRepository.save(mealLog);
        } catch (Exception e) {
            throw new MealException(MealErrorCode.LOG_SAVE_FAIL, e);
        }

        return convertToDto(savedMeal);
    }

    public List<MealResponseDto> getMealsByDay(Long userId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Meal> meals = mealRepository.findByUserIdAndEatTimeBetweenOrderByEatTimeAsc(userId, start, end);
        return meals.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<Meal> getMealById(Long id) {
        return mealRepository.findById(id);
    }

    public MealResponseDto updateMeal(Long mealId, Long userId, MealRequestDto dto) {
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new MealException(MealErrorCode.MEAL_NOT_FOUND));

        if (!meal.getUserId().equals(userId)) {
            throw new MealException(MealErrorCode.UNAUTHORIZED_ACCESS);
        }

        meal.setMealType(dto.getMealType());
        meal.setEatTime(dto.getEatTime());

        mealFoodRepository.deleteByMealId(meal.getId());

        List<MealFood> newMealFoods = new ArrayList<>();
        float totalCalories = 0f;

        for (MealRequestDto.FoodItem item : dto.getFoodList()) {
            Food food = foodRepository.findById(item.foodId())
                    .orElseThrow(() -> new MealException(MealErrorCode.FOOD_NOT_FOUND));

            float calories = food.getCalories() * (item.quantity() / food.getStandardAmount());

            MealFood mealFood = MealFood.builder()
                    .meal(meal)
                    .foodId(food.getId())
                    .quantity(item.quantity())
                    .calories(calories)
                    .build();

            newMealFoods.add(mealFood);
            totalCalories += calories;
        }

        meal.setMealFoods(newMealFoods);
        meal.setTotalCalories(totalCalories);

        mealRepository.save(meal);

        try {
            MealLog mealLog = MealLog.builder()
                    .mealId(meal.getId())
                    .userId(userId)
                    .action(MealLog.ActionType.UPDATE)
                    .snapshot(objectMapper.writeValueAsString(meal))
                    .actionTime(LocalDateTime.now())
                    .build();
            mealLogRepository.save(mealLog);
        } catch (Exception e) {
            throw new MealException(MealErrorCode.LOG_SAVE_FAIL, e);
        }

        return convertToDto(meal);
    }

    public MealResponseDto convertToDto(Meal meal) {
        List<FoodDetail> foodList = meal.getMealFoods().stream()
                .map(mf -> {
                    String foodName = foodRepository.findById(mf.getFoodId())
                            .map(Food::getName)
                            .orElse("Unknown");
                    return new FoodDetail(
                            mf.getFoodId(),
                            foodName,
                            mf.getQuantity(),
                            mf.getCalories()
                    );
                }).collect(Collectors.toList());

        String imageUrl = meal.getHasImage() != null && meal.getHasImage()
                ? mealImageRepository.findByMealId(meal.getId())
                .map(MealImage::getFilePath)
                .orElse(null)
                : null;

        return new MealResponseDto(
                meal.getId(),
                meal.getEatTime(),
                meal.getMealType(),
                meal.getTotalCalories(),
                meal.getHasImage(),
                foodList,
                imageUrl
        );
    }

    public void deleteMealById(Long id, Long userId) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new MealException(MealErrorCode.MEAL_NOT_FOUND));

        if (!meal.getUserId().equals(userId)) {
            throw new MealException(MealErrorCode.UNAUTHORIZED_ACCESS);
        }

        if (meal.getHasImage() != null && meal.getHasImage()) {
            mealImageRepository.findByMealId(meal.getId()).ifPresent(image -> {
                String path = "src/main/resources/static" + image.getFilePath();
                try {
                    Files.deleteIfExists(Paths.get(path));
                } catch (IOException e) {
                    throw new MealException(MealErrorCode.IMAGE_SAVE_FAIL, e);
                }
            });
        }

        mealRepository.delete(meal);

        try {
            MealLog mealLog = MealLog.builder()
                    .mealId(meal.getId())
                    .userId(userId)
                    .action(MealLog.ActionType.DELETE)
                    .snapshot(objectMapper.writeValueAsString(meal))
                    .actionTime(LocalDateTime.now())
                    .build();
            mealLogRepository.save(mealLog);
        } catch (Exception e) {
            throw new MealException(MealErrorCode.LOG_SAVE_FAIL, e);
        }
    }
}