package com.sihyun.pingpong.dto.room;

import com.sihyun.pingpong.domain.enums.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record RoomCreateRequestDto(
        @NotNull
        @Schema(description = "유저 ID", example = "1")
        Long userId,

        @NotNull
        @Schema(description = "방 타입 (단식: SINGLE, 복식: DOUBLE)", example = "SINGLE", defaultValue = "SINGLE")
        RoomType roomType,  // ✅ Enum 사용

        @NotNull
        @Schema(description = "방 제목", example = "재밌는 탁구 경기")
        String title
) {}
