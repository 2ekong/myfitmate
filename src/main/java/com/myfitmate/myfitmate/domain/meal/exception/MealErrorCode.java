package com.myfitmate.myfitmate.domain.meal.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MealErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    DUPLICATE_MEAL(HttpStatus.CONFLICT, "같은 날짜의 동일한 식사 유형이 이미 등록되어 있습니다."),
    FOOD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 음식은 존재하지 않습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "본인의 식단만 접근할 수 있습니다."),
    IMAGE_DIRECTORY_CREATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 디렉토리 생성 실패"),
    IMAGE_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 저장 실패"),
    LOG_SAVE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "식단 로그 저장 실패"),
    MEAL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 식단이 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;
}
