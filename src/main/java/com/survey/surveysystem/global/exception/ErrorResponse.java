package com.survey.surveysystem.global.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private final int status;
    private final String message;

    private  ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getHttpStatus().value(), errorCode.getMessage());
    }
}
