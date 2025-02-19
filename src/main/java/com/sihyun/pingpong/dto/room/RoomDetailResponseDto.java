package com.sihyun.pingpong.dto.room;

import io.swagger.v3.oas.annotations.media.Schema;

public record RoomDetailResponseDto(
        @Schema(description = "방 ID", example = "1")
        Long id,

        @Schema(description = "방 제목", example = "탁구 고수들 모임")
        String title,

        @Schema(description = "방장 ID", example = "1001")
        Long hostId,

        @Schema(description = "방 타입 (SINGLE: 단식, DOUBLE: 복식)", example = "SINGLE")
        String roomType,

        @Schema(description = "방 상태 (WAIT: 대기, PROGRESS: 진행 중, FINISH: 완료)", example = "WAIT")
        String status,

        @Schema(description = "방 생성 시간", example = "2025-02-18 12:34:56")
        String createdAt,

        @Schema(description = "방 업데이트 시간", example = "2025-02-18 12:34:56")
        String updatedAt
) {}
