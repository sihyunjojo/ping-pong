package com.sihyun.pingpong.dto.game;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record GameStartRequestDto(
    @Schema(description = "게임을 시작하는 유저 ID", example = "1")
    @NotNull Long userId
) {}
