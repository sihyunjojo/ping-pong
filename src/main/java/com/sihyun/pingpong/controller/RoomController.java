package com.sihyun.pingpong.controller;

import com.sihyun.pingpong.dto.ApiResponse;
import com.sihyun.pingpong.dto.room.RoomCreateRequestDto;
import com.sihyun.pingpong.dto.room.RoomDetailResponseDto;
import com.sihyun.pingpong.dto.room.RoomJoinRequestDto;
import com.sihyun.pingpong.dto.room.RoomLeaveRequestDto;
import com.sihyun.pingpong.dto.room.RoomListResponseDto;
import com.sihyun.pingpong.dto.room.TeamChangeRequestDto;
import com.sihyun.pingpong.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @PostMapping("/attention/{roomId}")
    @Operation(summary = "방 참가", description = "유저가 방에 참가합니다.")
    public ResponseEntity<ApiResponse<Void>> joinRoom(
        @Parameter(description = "참가할 방의 ID", required = true, example = "1")
        @PathVariable("roomId") Long roomId, 
        @RequestBody RoomJoinRequestDto request) {
        roomService.joinRoom(roomId, request);
        return ResponseEntity.ok(ApiResponse.res(200, "API 요청이 성공했습니다."));
    }
    
    @PostMapping("/out/{roomId}")
    @Operation(summary = "방 나가기", description = "유저가 방에서 나갑니다.")
    public ResponseEntity<ApiResponse<Void>> leaveRoom(
        @Parameter(description = "방 ID", example = "1")
        @PathVariable("roomId") Long roomId,
        @RequestBody RoomLeaveRequestDto request
    ) {
        roomService.leaveRoom(roomId, request);
        return ResponseEntity.ok(ApiResponse.res(200, "API 요청이 성공했습니다."));
    }

    @PutMapping("/{roomId}")
    @Operation(summary = "팀 변경", description = "유저가 현재 참가 중인 방에서 팀을 변경합니다.")
    public ResponseEntity<ApiResponse<Void>> changeTeam(
        @Parameter(description = "팀을 변경할 방의 ID", required = true, example = "1")
        @PathVariable("roomId") Long roomId,
        @RequestBody TeamChangeRequestDto request
    ) {
        roomService.changeTeam(roomId, request);
        return ResponseEntity.ok(ApiResponse.res(200, "API 요청이 성공했습니다."));
    }
}
