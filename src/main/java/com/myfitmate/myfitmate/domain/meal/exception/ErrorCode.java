package com.myfitmate.myfitmate.domain.meal.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),
    MEAL_NOT_FOUND(HttpStatus.NOT_FOUND, "식단 정보를 찾을 수 없습니다."),
    FOOD_NOT_FOUND(HttpStatus.NOT_FOUND, "음식 정보를 찾을 수 없습니다."),
    INVALID_FOOD_STANDARD(HttpStatus.BAD_REQUEST, "음식의 기준량이 잘못되었습니다."),
    DUPLICATE_MEAL(HttpStatus.CONFLICT, "해당 시간대에 이미 식사가 등록되어 있습니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");

    private final HttpStatus status;
    private final String message;
}
