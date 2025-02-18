package com.sihyun.pingpong.dto.user;

import com.sihyun.pingpong.domain.User;
import java.time.format.DateTimeFormatter;

public record UserResponseDto(
        Long id,
        Integer fakerId,
        String name,
        String email,
        String status,
        String createdAt,
        String updatedAt
) {
    public static UserResponseDto fromEntity(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return new UserResponseDto(
                user.getId(),
                user.getFakerId(),
                user.getName(),
                user.getEmail(),
                user.getStatus().name(),
                user.getCreatedAt().format(formatter),
                user.getUpdatedAt().format(formatter)
        );
    }
}
