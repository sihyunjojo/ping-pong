package com.sihyun.pingpong.controller;

import com.sihyun.pingpong.dto.team.TeamChangeRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sihyun.pingpong.dto.ApiResponse;
import com.sihyun.pingpong.service.TeamService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
@Tag(name = "5. Team", description = "팀 관련 API")
public class TeamController {
    
    private final TeamService teamService;
    
    @PutMapping("/{roomId}")
    @Operation(summary = "팀 변경", description = "유저가 현재 참가 중인 방에서 팀을 변경합니다.")
    public ResponseEntity<ApiResponse<Void>> changeTeam(
        @Parameter(description = "팀을 변경할 방의 ID", required = true, example = "1")
        @PathVariable("roomId") Long roomId,
        @Valid @RequestBody TeamChangeRequestDto request
    ) {
        teamService.changeTeam(roomId, request);
        return ResponseEntity.ok(ApiResponse.res(200, "API 요청이 성공했습니다."));
    }
}
