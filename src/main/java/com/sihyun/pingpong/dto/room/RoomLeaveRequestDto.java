package com.sihyun.pingpong.dto.room;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record RoomLeaveRequestDto(
        @NotNull
        @Schema(description = "방을 나가려는 유저의 ID", example = "2")
        Long userId
) {}
