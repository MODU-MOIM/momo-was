package com.example.momowas.response;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    /* HTTP 상태 코드: 적절한 HTTP 상태 코드를 매핑
    200: 성공
    400: 클라이언트 입력 오류
    401: 인증 실패
    403: 권한 부족
    404: 리소스 없음*/

    //성공
    SUCCESS(200, "G000", "요청이 성공적으로 처리되었습니다."),

    // 일반적인 예외
    NULL_POINT_ERROR(404, "G010", "NullPointerException 발생"),
    NOT_VALID_ERROR(404, "G011", "Validation Exception 발생"),

    // 인증 관련 예외
    INVALID_AUTHENTICATION(401, "A001", "인증에 실패했습니다."),
    EXPIRED_AUTHENTICATION(401, "A002", "인증이 만료되었습니다."),
    INVALID_PHONE_NUMBER(400, "A003", "유효하지 않은 전화번호입니다."),
    ALREADY_AUTHENTICATED(400, "A004", "이미 인증된 사용자입니다."),

    INVALID_TOKEN(401, "T001", "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(401, "T002", "토큰이 만료되었습니다."),
    TOKEN_MISSING(401, "T003", "토큰이 존재하지 않습니다."),

    // 사용자 관련 예외
    USER_NOT_FOUND(404, "U001", "사용자를 찾을 수 없습니다."),
    ALREADY_EXISTS(409, "U002", "이미 존재하는 정보입니다."),
    NOT_VERIFIED_YET(401, "U004", "아직 인증이 완료되지 않았습니다."),

    // SMS 관련 예외
    SMS_SEND_FAILED(500, "S001", "SMS 전송에 실패했습니다."),
    INVALID_VERIFICATION_CODE(400, "S002", "인증 코드가 유효하지 않습니다."),
    EXPIRED_VERIFICATION_CODE(400, "S003", "인증 코드가 만료되었습니다.");


    private final int status;
    private final String code;
    private final String message;

    ExceptionCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

