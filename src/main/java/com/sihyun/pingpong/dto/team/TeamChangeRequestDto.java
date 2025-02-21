package com.sihyun.pingpong.dto.team;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TeamChangeRequestDto(
        @NotNull
        @Schema(description = "팀을 변경할 유저 ID", example = "1")
        Long userId
) {}
