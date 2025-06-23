package com.myfitmate.myfitmate.domain.meal.service;

import com.myfitmate.myfitmate.domain.food.entity.Food;
import com.myfitmate.myfitmate.domain.food.repository.FoodRepository;
import com.myfitmate.myfitmate.domain.meal.dto.MealRequestDto;
import com.myfitmate.myfitmate.domain.meal.dto.MealResponseDto;
import com.myfitmate.myfitmate.domain.meal.entity.*;
import com.myfitmate.myfitmate.domain.meal.exception.MealErrorCode;
import com.myfitmate.myfitmate.domain.meal.exception.MealException;
import com.myfitmate.myfitmate.domain.meal.repository.*;
import com.myfitmate.myfitmate.domain.meal.util.MealImageUtil;
import com.myfitmate.myfitmate.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;
    private final MealFoodRepository mealFoodRepository;
    private final MealImageRepository mealImageRepository;
    private final MealLogRepository mealLogRepository;
    private final FoodRepository foodRepository;
    private final UserRepository userRepository;
    private final MealImageUtil mealImageUtil;

    // 식단 등록
    @Transactional
    public MealResponseDto registerMeal(MealRequestDto dto, Long userId, MultipartFile imageFile) {
        // 유효한 사용자 ID인지 확인
        validateUser(userId);

        // 같은 날 같은 시간대(아침/점심 등)에 이미 식단이 등록되어 있는지 확인
        validateDuplicateMeal(userId, dto);

        // Meal 엔티티를 생성하여 저장 (초기 totalCalories는 0)
        Meal meal = createAndSaveMeal(dto, userId, imageFile);

        // 음식 리스트 저장 및 총 칼로리 계산
        float totalCalories = saveMealFoods(meal, dto);
        meal.updateTotalCalories(totalCalories);

        // 이미지 파일이 있다면 저장
        if (isValidImage(imageFile)) {
            saveMealImage(meal, imageFile);
        }

        // 로그 저장
        saveMealLog(meal, userId, MealLog.ActionType.CREATE);

        // 클라이언트에게 응답할 DTO 반환
        return toResponse(meal);
    }

    // 식단 수정
    @Transactional
    public MealResponseDto updateMeal(Long mealId, MealRequestDto dto, MultipartFile imageFile, Long userId) {
        // 사용자 인증 및 해당 사용자의 식단인지 확인
        Meal meal = getAuthorizedMeal(mealId, userId);

        // 기존 음식 정보 제거
        mealFoodRepository.deleteByMeal(meal);

        // 새 음식 정보 저장 및 총 칼로리 갱신
        float totalCalories = saveMealFoods(meal, dto);
        meal.updateMeal(dto.getEatTime(), dto.getMealType(), totalCalories, isValidImage(imageFile));

        // 이미지 파일이 있다면 이미지 교체
        if (isValidImage(imageFile)) {
            updateMealImage(meal, imageFile);
        }

        // 로그 저장
        saveMealLog(meal, userId, MealLog.ActionType.UPDATE);
        return toResponse(meal);
    }

    // 식단 삭제
    @Transactional
    public void deleteMeal(Long mealId, Long userId) {
        Meal meal = getAuthorizedMeal(mealId, userId);
        mealFoodRepository.deleteByMeal(meal);
        deleteMealImageIfExists(meal);
        saveMealLog(meal, userId, MealLog.ActionType.DELETE);
        mealRepository.delete(meal);
    }

    // 사용자 존재 여부 확인
    private void validateUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new MealException(MealErrorCode.USER_NOT_FOUND);
        }
    }

    // 중복 식단 검사 (같은 날짜와 식사 타입 중복 등록 방지)
    private void validateDuplicateMeal(Long userId, MealRequestDto dto) {
        LocalDateTime start = dto.getEatTime().toLocalDate().atStartOfDay();
        LocalDateTime end = dto.getEatTime().toLocalDate().atTime(LocalTime.MAX);
        boolean exists = mealRepository.existsByUserIdAndEatTimeBetweenAndMealType(userId, start, end, dto.getMealType());
        if (exists) {
            throw new MealException(MealErrorCode.DUPLICATE_MEAL);
        }
    }

    // Meal 엔티티 생성 후 DB 저장
    private Meal createAndSaveMeal(MealRequestDto dto, Long userId, MultipartFile imageFile) {
        Meal meal = Meal.builder()
                .userId(userId)
                .eatTime(dto.getEatTime())
                .mealType(dto.getMealType())
                .hasImage(isValidImage(imageFile))
                .totalCalories(0f)
                .build();
        return mealRepository.save(meal);
    }

    // MealFood 엔티티 생성 및 총 칼로리 계산
    private float saveMealFoods(Meal meal, MealRequestDto dto) {
        float totalCalories = 0f;
        for (MealRequestDto.FoodInfo foodInfo : dto.getFoodList()) {
            Food food = foodRepository.findById(foodInfo.getFoodId())
                    .orElseThrow(() -> new MealException(MealErrorCode.FOOD_NOT_FOUND));
            float foodCalories = food.getCalories() * foodInfo.getQuantity();
            MealFood mealFood = MealFood.builder()
                    .meal(meal)
                    .food(food)
                    .quantity(foodInfo.getQuantity())
                    .calories(foodCalories)
                    .build();
            mealFoodRepository.save(mealFood);
            totalCalories += foodCalories;
        }
        return totalCalories;
    }

    // 이미지 저장 및 MealImage 엔티티 저장
    private void saveMealImage(Meal meal, MultipartFile imageFile) {
        try {
            String path = mealImageUtil.saveImage(imageFile);
            MealImage image = MealImage.builder()
                    .meal(meal)
                    .filePath(path)
                    .build();
            mealImageRepository.save(image);
        } catch (Exception e) {
            throw new MealException(MealErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    // 기존 이미지 삭제 후 새로운 이미지로 교체
    private void updateMealImage(Meal meal, MultipartFile imageFile) {
        mealImageRepository.findByMeal(meal).ifPresent(existing -> {
            mealImageUtil.deleteImage(existing.getFilePath());
            mealImageRepository.delete(existing);
        });
        saveMealImage(meal, imageFile);
    }

    // 이미지 존재 시 삭제
    private void deleteMealImageIfExists(Meal meal) {
        mealImageRepository.findByMeal(meal).ifPresent(image -> {
            mealImageUtil.deleteImage(image.getFilePath());
            mealImageRepository.delete(image);
        });
    }

    // 로그 저장 (등록, 수정, 삭제 구분)
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

    // 사용자가 소유한 식단인지 확인
    private Meal getAuthorizedMeal(Long mealId, Long userId) {
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new MealException(MealErrorCode.MEAL_NOT_FOUND));
        if (!meal.getUserId().equals(userId)) {
            throw new MealException(MealErrorCode.UNAUTHORIZED_ACCESS);
        }
        return meal;
    }

    // Meal → DTO 변환
    private MealResponseDto toResponse(Meal meal) {
        return MealResponseDto.fromEntity(
                meal,
                mealFoodRepository.findByMeal(meal),
                mealImageRepository.findByMeal(meal).orElse(null)
        );
    }

    // 이미지 유효성 검사
    private boolean isValidImage(MultipartFile file) {
        return file != null && !file.isEmpty();
    }

    // 전체 식단 조회
    @Transactional(readOnly = true)
    public List<MealResponseDto> getAllMeals(Long userId) {
        validateUser(userId);
        List<Meal> meals = mealRepository.findByUserId(userId);
        return meals.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // 키워드 기반 식단 검색
    @Transactional(readOnly = true)
    public List<MealResponseDto> searchMeals(Long userId, String keyword) {
        validateUser(userId);
        List<Meal> meals = mealRepository.findByUserIdAndKeyword(userId, keyword);
        return meals.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
