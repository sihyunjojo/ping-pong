package com.sihyun.pingpong.exception;

/**
 * RoomService 관련 예외 처리
 */
public class RoomServiceException extends RuntimeException {
    public RoomServiceException(String message) {
        super(message);
    }
}
