package com.sihyun.pingpong.exception;

import com.sihyun.pingpong.dto.ApiResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 잘못된 요청(유저가 존재하지 않음) 예외 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(201).body(ApiResponse.res(201, ex.getMessage()));
    }

    /**
     * 비즈니스 로직 위반 (방을 생성할 수 없는 상태 등) 예외 처리
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalStateException(IllegalStateException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(201).body(ApiResponse.res(201, ex.getMessage()));
    }

    /**
     * 예상하지 못한 에러 처리 (500 에러)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.res(500, "서버 내부 오류가 발생했습니다."));
    }
}
