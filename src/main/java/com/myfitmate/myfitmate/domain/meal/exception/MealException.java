package com.myfitmate.myfitmate.domain.meal.exception;

import lombok.Getter;

@Getter
public class MealException extends RuntimeException {

    private final MealErrorCode errorCode;

    public MealException(MealErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
