package com.sihyun.pingpong.service;

import com.sihyun.pingpong.domain.User;
import com.sihyun.pingpong.dto.user.UserListResponseDto;
import com.sihyun.pingpong.dto.user.UserResponseDto;
import com.sihyun.pingpong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserListResponseDto getAllUsersWithPaging(int page, int size) {
        Page<User> userPage = userRepository.findAll(PageRequest.of(page, size));
        List<UserResponseDto> userList = userPage.getContent().stream()
                .map(UserResponseDto::fromEntity)
                .toList();

        return new UserListResponseDto(
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userList
        );
    }
}
