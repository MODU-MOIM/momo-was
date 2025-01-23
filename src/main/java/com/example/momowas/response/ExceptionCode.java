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
    BAD_REQUEST_ERROR(400, "G001", "Bad Request Exception"),
    REQUEST_BODY_MISSING_ERROR(400, "G002", "Required request body is missing"),
    INVALID_TYPE_VALUE(400, "G003", " Invalid Type Value"),
    MISSING_REQUEST_PARAMETER_ERROR(400, "G004", "Missing Servlet RequestParameter Exception"),
    IO_ERROR(400, "G005", "I/O Exception"),
    JSON_PARSE_ERROR(400, "G006", "JsonParseException"),
    JACKSON_PROCESS_ERROR(400, "G007", "com.fasterxml.jackson.core Exception"),
    FORBIDDEN_ERROR(403, "G008", "Forbidden Exception"),
    NOT_FOUND_ERROR(404, "G009", "Not Found Exception"),
    NOT_VALID_HEADER_ERROR(404, "G012", "Header에 데이터가 존재하지 않는 경우 "),
    INTERNAL_SERVER_ERROR(500, "G999", "Internal Server Error Exception"),

    // 인증 관련 예외
    INVALID_AUTHENTICATION(401, "A001", "인증에 실패했습니다."),
    EXPIRED_AUTHENTICATION(401, "A002", "인증이 만료되었습니다."),
    INVALID_PHONE_NUMBER(400, "A003", "유효하지 않은 전화번호입니다."),
    ALREADY_AUTHENTICATED(400, "A004", "이미 인증된 사용자입니다."),

    // 인가 관련 예외
    ACCESS_DENIED(403, "AU001", "권한이 없습니다."),

    INVALID_TOKEN(401, "T001", "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(401, "T002", "토큰이 만료되었습니다."),
    TOKEN_MISSING(401, "T003", "토큰이 존재하지 않습니다."),

    // 사용자 관련 예외
    USER_NOT_FOUND(404, "U001", "사용자를 찾을 수 없습니다."),
    ALREADY_EXISTS(409, "U002", "이미 존재하는 정보입니다."),
    NOT_VERIFIED_YET(401, "U004", "아직 인증이 완료되지 않았습니다."),
    USER_MISMATCH(403,"U005", "해당 작업을 허용할 수 없습니다."),

    // SMS 관련 예외
    SMS_SEND_FAILED(500, "S001", "SMS 전송에 실패했습니다."),
    INVALID_VERIFICATION_CODE(400, "S002", "인증 코드가 유효하지 않습니다."),
    EXPIRED_VERIFICATION_CODE(400, "S003", "인증 코드가 만료되었습니다."),

    //채팅방 예외
    CHATROOM_NOT_FOUND(404, "C001", "채팅방을 찾을 수 없습니다."),

    //일정 예외
    SCHEDULE_NOT_FOUND(404, "SC001", "일정을 찾을 수 없습니다."),

    //크루 관련 예외
    ALREADY_EXISTS_CREW(409, "C001", "이미 존재하는 크루입니다."),
    NOT_FOUND_CREW(404, "C002", "해당 크루를 찾을 수 없습니다."),
    CREW_FULL(409, "C003","크루 정원이 꽉 찼습니다."),

    //크루 멤버 관련 예외
    NOT_FOUND_CREW_MEMBER(404, "CM001", "해당 크루 멤버를 찾을 수 없습니다."),
    ALREADY_JOINED_CREW(409, "CM002", "이미 해당 크루에 가입한 사용자입니다."),
    INVALID_CREW_JOIN_CONDITION(409, "CM004", "크루 가입 조건에 맞지 않습니다."),
    ALREADY_REQUESTED_TO_JOIN_CREW(409, "CM002", "이미 해당 크루에 가입 요청을 했습니다."),
    NOT_FOUND_JOIN_REQUEST(409, "CM003","해당 크루 가입 요청을 찾을 수 없습니다."),

    //지역 관련 예외
    NOT_FOUND_REGION(404, "R001", "해당 지역이 존재하지 않습니다."),

    //공지 관련 예외
    NOT_FOUND_NOTICE(404, "N001", "해당 공지가 존재하지 않습니다."),
    NOT_FOUND_VOTE(404, "N002", "해당 투표가 존재하지 않습니다."),
    NOT_FOUND_VOTE_PARTICIPANT(404, "N003", "해당 투표자가 존재하지 않습니다."),
    ALREADY_PARTICIPATE_VOTE(409, "N004", "이미 참여한 투표입니다."),

    //피드 관련 예외
    NOT_FOUND_FEED(404, "F001","해당 피드가 존재하지 않습니다."),
    NOT_FOUND_FEED_PHOTO(404, "F001","해당 피드 사진이 존재하지 않습니다."),

    //댓글 관련 예외
    NOT_FOUNT_COMMENT(404, "CO001","해당 댓글이 존재하지 않습니다."),

    //좋아요 관련 예외
    ALREADY_LIKE_FEED(409, "L004", "이미 좋아요를 누른 게시글입니다.");


    private final int status;
    private final String code;
    private final String message;

    ExceptionCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

