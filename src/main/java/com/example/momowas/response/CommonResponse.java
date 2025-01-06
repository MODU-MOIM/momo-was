package com.example.momowas.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"status", "code", "message", "data"})
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
