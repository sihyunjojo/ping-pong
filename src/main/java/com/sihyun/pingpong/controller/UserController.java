package com.sihyun.pingpong.controller;

import com.sihyun.pingpong.dto.ApiResponse;
import com.sihyun.pingpong.dto.user.UserListResponseDto;
import com.sihyun.pingpong.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User", description = "유저 관련 API")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "유저  조회", description = "페이징을 이용해 유저 전체를 조회합니다.")
    public ResponseEntity<ApiResponse<UserListResponseDto>> getAllUsersWithPaging(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
) {
        UserListResponseDto users = userService.getAllUsersWithPaging(page, size);
        return ResponseEntity.ok(ApiResponse.res(200, "API 요청이 성공했습니다.", users));
    }
}