package com.sihyun.pingpong.dto.room;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record RoomListResponseDto(
        @Schema(description = "전체 방 개수", example = "20")
        long totalElements,

        @Schema(description = "총 페이지 수", example = "2")
        int totalPages,

        @Schema(description = "방 목록")
        List<RoomResponseDto> roomList
) {}
