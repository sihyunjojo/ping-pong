package com.sihyun.pingpong.controller;

import com.sihyun.pingpong.dto.ApiResponse;
import com.sihyun.pingpong.dto.room.RoomCreateRequestDto;
import com.sihyun.pingpong.dto.room.RoomDetailResponseDto;
import com.sihyun.pingpong.dto.room.RoomListResponseDto;
import com.sihyun.pingpong.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
@Tag(name = "3. Room", description = "게임 방 관련 API")
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    @Operation(summary = "방 생성", description = "유저가 새로운 방을 생성합니다.")
    public ResponseEntity<ApiResponse<Void>> createRoom(@RequestBody RoomCreateRequestDto request) {
        roomService.createRoom(request);
        return ResponseEntity.ok(ApiResponse.res(200, "API 요청이 성공했습니다."));
    }

    @GetMapping
    @Operation(summary = "방 페이징 조회", description = "페이징을 이용해 모든 방을 조회합니다.")
    public ResponseEntity<ApiResponse<RoomListResponseDto>> getAllRooms(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        RoomListResponseDto rooms = roomService.getAllRoomsWithPaging(page, size);
        return ResponseEntity.ok(ApiResponse.res(200, "API 요청이 성공했습니다.", rooms));
    }

    @GetMapping("/{roomId}")
    @Operation(summary = "방 상세 조회", description = "방 ID를 이용해 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<RoomDetailResponseDto>> getRoomDetail(
        @Parameter(description = "조회할 방의 ID", required = true, example = "1")
        @PathVariable("roomId") Long roomId
    ) {
        RoomDetailResponseDto roomDetail = roomService.getRoomDetail(roomId);
        return ResponseEntity.ok(ApiResponse.res(200, "API 요청이 성공했습니다.", roomDetail));
    }
    
}
