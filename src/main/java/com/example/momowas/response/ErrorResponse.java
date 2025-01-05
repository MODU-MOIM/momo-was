package com.example.momowas.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse<T> {
    private int status;
    private String code;
    private String message;
    private T data; // 에러 상세 정보

    @Builder
    public ErrorResponse(final ExceptionCode code, final T data) {
        this.status = code.getStatus();
        this.code = code.getCode();
        this.message = code.getMessage();
        this.data = data; // 유효성 검사 에러 등의 상세 정보
    }

    public static ErrorResponse<List<FieldError>> of(final ExceptionCode code, final List<FieldError> errors) {
        return new ErrorResponse<>(code, errors);
    }

    public static ErrorResponse<String> of(final ExceptionCode code, final String reason) {
        return new ErrorResponse<>(code, reason);
    }

    public static ErrorResponse<Void> of(final ExceptionCode code) {
        return new ErrorResponse<>(code, null);
    }

    @Data
    public static class FieldError {
        private final String field;
        private final String value;
        private final String reason;

        @Builder
        public FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static FieldError of(final String field, final Object value, final String reason) {
            return new FieldError(
                    field,
                    value == null ? "" : value.toString(),
                    reason
            );
        }
    }
}
