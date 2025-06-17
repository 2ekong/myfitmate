package com.myfitmate.myfitmate.domain.food.exception;

import lombok.Getter;

@Getter
public class FoodException extends RuntimeException {

    private final FoodErrorCode errorCode;

    public FoodException(FoodErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public FoodException(FoodErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
