package com.myfitmate.myfitmate.domain.meal.exception;

import lombok.Getter;

@Getter
public class MealException extends RuntimeException {

    private final ErrorCode errorCode;

    public MealException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
