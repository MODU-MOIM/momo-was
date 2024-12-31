package com.example.momowas.error;

import lombok.Builder;
import lombok.Getter;

@Getter
public enum ExceptionCode {

    NULL_POINT_ERROR(404, "G010", "NullPointerException 발생"),
    NOT_VALID_ERROR(404, "G011", "Validation Exception 발생");

    // 1. status = 날려줄 상태코드
    // 2. code = 해당 오류가 어느부분과 관련있는지 카테고리화 해주는 코드. 예외 원인 식별하기 편하기에 추가
    // 3. message = 발생한 예외에 대한 설명.

    private final int status;
    private final String code;
    private final String message;

    ExceptionCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
