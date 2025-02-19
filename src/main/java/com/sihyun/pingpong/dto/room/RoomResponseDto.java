package com.sihyun.pingpong.dto.room;

import com.sihyun.pingpong.domain.Room;
import com.sihyun.pingpong.domain.enums.RoomStatus;
import com.sihyun.pingpong.domain.enums.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;

public record RoomResponseDto(
        @Schema(description = "방 ID", example = "1")
        Long id,

        @Schema(description = "방 제목", example = "친선 경기 모집")
        String title,

        @Schema(description = "방장 ID", example = "10")
        Long hostId,

        @Schema(description = "방 타입 (단식: SINGLE, 복식: DOUBLE)", example = "SINGLE")
        RoomType roomType,

        @Schema(description = "방 상태 (WAIT, PROGRESS, FINISH)", example = "WAIT")
        RoomStatus status
) {
    public static RoomResponseDto fromEntity(Room room) {
        return new RoomResponseDto(
                room.getId(),
                room.getTitle(),
                room.getHost().getId(),
                room.getRoomType(),
                room.getStatus()
        );
    }
}
