package com.myfitmate.myfitmate.domain.meal.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.myfitmate.myfitmate.domain.meal")
public class GlobalExceptionHandler {

    @ExceptionHandler(MealException.class)
    public ResponseEntity<ErrorResponse> handleMealException(MealException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(errorResponse);
    }

    // 내부 static 클래스로 정의 (별도 파일 없음)
    private static class ErrorResponse {
        private final String message;
        private final int status;

        public ErrorResponse(ErrorCode errorCode) {
            this.message = errorCode.getMessage();
            this.status = errorCode.getStatus().value();
        }

        public String getMessage() {
            return message;
        }

        public int getStatus() {
            return status;
        }
    }
}
