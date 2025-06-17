package com.myfitmate.myfitmate.domain.food.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FoodErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."),
    DUPLICATE_FOOD(HttpStatus.CONFLICT, "이미 등록된 음식입니다."),
    FOOD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 음식이 존재하지 않습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "본인의 음식만 접근할 수 있습니다."),
    FOOD_REGISTER_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "음식 등록 실패"),
    FOOD_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "음식 삭제 실패");

    private final HttpStatus status;
    private final String message;
}
