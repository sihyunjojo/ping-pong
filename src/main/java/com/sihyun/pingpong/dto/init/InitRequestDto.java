package com.sihyun.pingpong.dto.init;

import io.swagger.v3.oas.annotations.media.Schema;

public record InitRequestDto(
        @Schema(description = "Seed 값", defaultValue = "1") Integer seed,
        @Schema(description = "생성할 유저 수", defaultValue = "10") Integer quantity
) {}
