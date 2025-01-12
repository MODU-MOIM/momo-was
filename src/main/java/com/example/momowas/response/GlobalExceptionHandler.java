package com.example.momowas.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.shaded.gson.JsonParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * [Exception] 비즈니스 로직에 예외가 발생하는 경우
     *
     * @param ex BusinessException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(BusinessException ex) {
        ExceptionCode errorCode = ex.getExceptionCode();
        log.error("Business Exception Error", ex);
        final ErrorResponse response = ErrorResponse.of(errorCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /**
     * [Exception] 인가 거부와 관련된 예외가 발생하는 경우
     *
     * @param ex AccessDeniedException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("handleAccessDeniedException : {}", ex);
        final ErrorResponse response = ErrorResponse.of(ExceptionCode.ACCESS_DENIED);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /**
     * [Exception] API 호출 시 '객체' 혹은 '파라미터' 데이터 값이 유효하지 않은 경우
     *
     * @param ex MethodArgumentNotValidException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("handleMethodArgumentNotValidException", ex);
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getField()).append(":");
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append(", ");
        }
        final ErrorResponse response = ErrorResponse.of(ExceptionCode.NOT_VALID_ERROR, String.valueOf(stringBuilder));
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /**
     * [Exception] API 호출 시 'Header' 내에 데이터 값이 유효하지 않은 경우
     *
     * @param ex MissingRequestHeaderException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        log.error("MissingRequestHeaderException", ex);
        final ErrorResponse response = ErrorResponse.of(ExceptionCode.REQUEST_BODY_MISSING_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /**
     * [Exception] 클라이언트에서 Body로 '객체' 데이터가 넘어오지 않았을 경우
     *
     * @param ex HttpMessageNotReadableException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException", ex);
        final ErrorResponse response = ErrorResponse.of(ExceptionCode.REQUEST_BODY_MISSING_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /**
     * [Exception] 클라이언트에서 request로 '파라미터로' 데이터가 넘어오지 않았을 경우
     *
     * @param ex MissingServletRequestParameterException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderExceptionException(
            MissingServletRequestParameterException ex) {
        log.error("handleMissingServletRequestParameterException", ex);
        final ErrorResponse response = ErrorResponse.of(ExceptionCode.MISSING_REQUEST_PARAMETER_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /**
     * [Exception] 잘못된 서버 요청일 경우 발생한 경우
     *
     * @param e HttpClientErrorException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    protected ResponseEntity<ErrorResponse> handleBadRequestException(HttpClientErrorException e) {
        log.error("HttpClientErrorException.BadRequest", e);
        final ErrorResponse response = ErrorResponse.of(ExceptionCode.BAD_REQUEST_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }


    /**
     * [Exception] 잘못된 주소로 요청 한 경우
     *
     * @param e NoHandlerFoundException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoHandlerFoundExceptionException(NoHandlerFoundException e) {
        log.error("handleNoHandlerFoundExceptionException", e);
        final ErrorResponse response = ErrorResponse.of(ExceptionCode.NOT_FOUND_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }


    /**
     * [Exception] NULL 값이 발생한 경우
     *
     * @param e NullPointerException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e) {
        log.error("handleNullPointerException", e);
        final ErrorResponse response = ErrorResponse.of(ExceptionCode.NULL_POINT_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /**
     * com.google.gson 내에 Exception 발생하는 경우
     *
     * @param ex JsonParseException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(JsonParseException.class)
    protected ResponseEntity<ErrorResponse> handleJsonParseExceptionException(JsonParseException ex) {
        log.error("handleJsonParseExceptionException", ex);
        final ErrorResponse response = ErrorResponse.of(ExceptionCode.JSON_PARSE_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /**
     * com.fasterxml.jackson.core 내에 Exception 발생하는 경우
     *
     * @param ex JsonProcessingException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(JsonProcessingException.class)
    protected ResponseEntity<ErrorResponse> handleJsonProcessingException(JsonProcessingException ex) {
        log.error("handleJsonProcessingException", ex);
        final ErrorResponse response = ErrorResponse.of(ExceptionCode.REQUEST_BODY_MISSING_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

    /**
     * [Exception] 모든 Exception 경우 발생
     *
     * @param ex Exception
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(Exception.class)
    protected final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        log.error("Exception", ex);
        final ErrorResponse response = ErrorResponse.of(ExceptionCode.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }

}