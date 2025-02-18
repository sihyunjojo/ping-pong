package com.sihyun.pingpong.controller;

import com.sihyun.pingpong.dto.ApiResponse;
import com.sihyun.pingpong.dto.room.RoomCreateRequestDto;
import com.sihyun.pingpong.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
@Tag(name = "Room", description = "게임 방 관련 API")
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    @Operation(summary = "방 생성", description = "유저가 새로운 방을 생성합니다.")
    public ResponseEntity<ApiResponse<Void>> createRoom(@RequestBody RoomCreateRequestDto request) {
        roomService.createRoom(request);
        return ResponseEntity.ok(ApiResponse.res(200, "API 요청이 성공했습니다."));
    }
}
