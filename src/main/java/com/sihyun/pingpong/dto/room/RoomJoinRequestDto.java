package com.sihyun.pingpong.dto.room;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record RoomJoinRequestDto(
        @Schema(description = "참가할 유저 ID", example = "2")
        @NotNull
        Long userId
) {}
