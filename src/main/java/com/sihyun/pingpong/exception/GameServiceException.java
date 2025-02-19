package com.sihyun.pingpong.exception;

/**
 * GameService 관련 예외 처리
 */
public class GameServiceException extends RuntimeException {
    public GameServiceException(String message) {
        super(message);
    }
}
