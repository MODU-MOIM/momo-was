package com.example.momowas.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonResponse<T> {
    private int status;
    private String code;
    private String message;
    private T data;

    public static <T> CommonResponse<T> of(ExceptionCode exceptionCode, T data) {
        return new CommonResponse<>(
                exceptionCode.getStatus(),
                exceptionCode.getCode(),
                exceptionCode.getMessage(),
                data
        );
    }
}
