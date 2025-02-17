package com.sihyun.pingpong.dto;

public record DefaultResponse<T>(int code, String message, T data) {

    public static <T> DefaultResponse<T> res(final int code, final String message) {
        return new DefaultResponse<>(code, message, null);
    }

    public static <T> DefaultResponse<T> res(final int code, final String message, final T t) {
        return new DefaultResponse<>(code, message, t);
    }
}
