package com.sihyun.pingpong.dto.user;

import java.util.List;

public record UserListResponseDto(
        long totalElements,
        int totalPages,
        List<UserResponseDto> userList
) {}
