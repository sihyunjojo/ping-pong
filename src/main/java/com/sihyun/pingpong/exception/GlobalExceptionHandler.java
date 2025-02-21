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
     * RoomService, GameService에서 발생한 예외 처리 (201 응답)
     */
    @ExceptionHandler({RoomServiceException.class, GameServiceException.class})
    public ResponseEntity<ApiResponse<Void>> handleServiceException(RuntimeException ex) {
        log.error("[SERVICE ERROR] {}", ex.getMessage());
        return ResponseEntity.status(200).body(ApiResponse.res(201, "불가능한 요청입니다."));
    }


    /**
     * 예상하지 못한 에러 처리 (500 에러)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.res(500, "에러가 발생했습니다."));
    }
}
