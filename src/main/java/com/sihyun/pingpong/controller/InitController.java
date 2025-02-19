package com.sihyun.pingpong.controller;

import com.sihyun.pingpong.dto.ApiResponse;
import com.sihyun.pingpong.dto.init.InitRequestDto;
import com.sihyun.pingpong.service.InitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/init")
@Tag(name = "Init", description = "데이터 초기화 API")
@RequiredArgsConstructor
public class InitController {

    private final InitService initService;

    @PostMapping()
    @Operation(summary = "초기화 API", description = "모든 데이터를 삭제하고 FakerAPI에서 새로운 데이터를 가져옵니다.")
    public ResponseEntity<ApiResponse<Void>> initDatabase(@RequestBody InitRequestDto request) {
        initService.initializeDatabase(request);
        return ResponseEntity.ok(ApiResponse.res(200, "API 요청이 성공했습니다."));
    }
}
