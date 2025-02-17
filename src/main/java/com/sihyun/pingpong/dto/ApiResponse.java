package com.sihyun.pingpong.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T result;

    public static <T> ApiResponse<T> res(final int code, final String message) {
        return new ApiResponse<>(code, message, null);
    }

    public static <T> ApiResponse<T> res(final int code, final String message, final T t) {
        return new ApiResponse<>(code, message, t);
    }
}
