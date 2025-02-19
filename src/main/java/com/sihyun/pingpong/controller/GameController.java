package com.sihyun.pingpong.controller;

import com.sihyun.pingpong.dto.ApiResponse;
import com.sihyun.pingpong.dto.game.GameStartRequestDto;
import com.sihyun.pingpong.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
@Tag(name = "4. Game", description = "게임 진행 관련 API")
public class GameController {

    private final GameService gameService;

    @PutMapping("/start/{roomId}")
    @Operation(summary = "게임 시작", description = "방장이 게임을 시작합니다.")
    public ResponseEntity<ApiResponse<Void>> startGame(
        @Parameter(description = "게임을 시작할 방의 ID", required = true, example = "1")
        @PathVariable("roomId") Long roomId,
        @RequestBody GameStartRequestDto request
    ) {
        gameService.startGame(roomId, request);
        return ResponseEntity.ok(ApiResponse.res(200, "API 요청이 성공했습니다."));
    }
}
