package com.sihyun.pingpong.controller;

import com.sihyun.pingpong.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Tag(name = "0. Health", description = "서버 상태 체크 API")
public class HealthCheckController {


    @GetMapping
    @Operation(summary = "서버 상태 체크", description = "서버의 상태를 체크합니다.")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        return ResponseEntity.ok(ApiResponse.res(200, "API 요청이 성공했습니다."));
    }
}
