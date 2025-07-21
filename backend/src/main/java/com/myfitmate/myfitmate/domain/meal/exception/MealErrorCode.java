package com.myfitmate.myfitmate.domain.meal.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MealErrorCode {

    USER_NOT_FOUND("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_MEAL("해당 시간대에 이미 등록된 식사가 존재합니다.", HttpStatus.CONFLICT),
    FOOD_NOT_FOUND("음식 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    FILE_UPLOAD_FAILED("파일 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    MEAL_NOT_FOUND("식단 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_ACCESS("해당 식단에 대한 접근 권한이 없습니다.", HttpStatus.FORBIDDEN);

    private final String message;
    private final HttpStatus status;
}
