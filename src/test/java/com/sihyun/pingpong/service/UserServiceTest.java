package com.sihyun.pingpong.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import com.sihyun.pingpong.domain.User;
import com.sihyun.pingpong.domain.enums.UserStatus;
import com.sihyun.pingpong.dto.user.UserListResponseDto;
import com.sihyun.pingpong.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private List<User> mockUsers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock User 데이터 생성
        mockUsers = List.of(
                User.builder().id(1L).name("User1").status(UserStatus.ACTIVE).email("user1@example.com").fakerId(1).build(),
                User.builder().id(2L).name("User2").status(UserStatus.ACTIVE).email("user2@example.com").fakerId(2).build(),
                User.builder().id(3L).name("User3").status(UserStatus.ACTIVE).email("user3@example.com").fakerId(3).build(),
                User.builder().id(4L).name("User4").status(UserStatus.ACTIVE).email("user4@example.com").fakerId(4).build(),
                User.builder().id(5L).name("User5").status(UserStatus.ACTIVE).email("user5@example.com").fakerId(5).build()
        );

        // ✅ createdAt과 updatedAt을 모두 설정
        mockUsers.forEach(user -> {
            ReflectionTestUtils.setField(user, "createdAt", LocalDateTime.now());
            ReflectionTestUtils.setField(user, "updatedAt", LocalDateTime.now());
        });
    }

    @ParameterizedTest
    @CsvSource({
            "0, 3, 5, 3, 2",  // ✅ 첫 번째 페이지 (0번 페이지), 크기 3 → 총 5개 중 3개 반환, 총 페이지 수는 2
            "2, 3, 5, 0, 2",  // ✅ 범위 초과 페이지 (빈 결과), 총 페이지 수는 2
            "0, 10, 5, 5, 1"  // ✅ 페이지 크기가 너무 큰 경우 → 5개만 반환, 총 페이지 수는 1
    })
    void getAllUsersWithPaging(int page, int size, long allCount, long expectedCount, int expectedTotalPages) {
        // Mocking 데이터 설정
        List<User> pagedUsers = mockUsers.stream()
                .skip((long) page * size)
                .limit(size)
                .toList();
        Page<User> mockPage = new PageImpl<>(pagedUsers, PageRequest.of(page, size), mockUsers.size());

        when(userRepository.findAll(PageRequest.of(page, size))).thenReturn(mockPage);

        // 테스트 실행
        UserListResponseDto response = userService.getAllUsersWithPaging(page, size);

        // 검증
        assertEquals(allCount, response.totalElements(), "총 유저 개수가 예상과 다름");
        assertEquals(expectedTotalPages, response.totalPages(), "총 페이지 수가 예상과 다름");
        assertEquals(expectedCount, response.userList().size(), "반환된 유저 리스트 크기가 예상과 다름");

        // Mock 검증 (findAll 호출 여부 확인)
        verify(userRepository, times(1)).findAll(PageRequest.of(page, size));
    }
}
