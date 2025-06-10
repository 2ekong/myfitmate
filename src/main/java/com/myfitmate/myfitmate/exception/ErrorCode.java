package com.myfitmate.myfitmate.exception;

public enum ErrorCode {
    USER_NOT_FOUND("사용자를 찾을 수 없습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
