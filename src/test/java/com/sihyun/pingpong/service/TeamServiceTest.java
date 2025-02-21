package com.sihyun.pingpong.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.sihyun.pingpong.domain.Room;
import com.sihyun.pingpong.domain.User;
import com.sihyun.pingpong.domain.UserRoom;
import com.sihyun.pingpong.domain.enums.RoomStatus;
import com.sihyun.pingpong.domain.enums.RoomType;
import com.sihyun.pingpong.domain.enums.Team;
import com.sihyun.pingpong.dto.room.TeamChangeRequestDto;
import com.sihyun.pingpong.repository.RoomRepository;
import com.sihyun.pingpong.repository.UserRepository;
import com.sihyun.pingpong.repository.UserRoomRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TeamServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoomRepository userRoomRepository;

    @InjectMocks
    private TeamService teamService;

    private Room testRoom;
    private User testUser;
    private UserRoom testUserRoom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 테스트용 Room 객체 생성
        testRoom = Room.builder()
                .id(1L)
                .status(RoomStatus.WAIT) // ✅ 대기 상태
                .roomType(RoomType.SINGLE) // ✅ 단식: 최대 2명
                .build();

        // 테스트용 User 객체 생성
        testUser = User.builder()
                .id(1L)
                .name("TestUser")
                .build();
    }

    @Test
    void changeTeam_Success_RED_to_BLUE() {
        // 🔴 기존 팀이 RED → 🔵 BLUE로 변경
        testUserRoom = UserRoom.builder()
                .user(testUser)
                .room(testRoom)
                .team(Team.RED) // 초기 팀 RED
                .build();

        // Mock 설정
        when(roomRepository.findById(1L)).thenReturn(Optional.of(testRoom));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRoomRepository.findByUserAndRoom(testUser, testRoom)).thenReturn(Optional.of(testUserRoom));
        when(userRoomRepository.countByRoomAndTeam(testRoom, Team.BLUE)).thenReturn(0L); // BLUE 팀원이 0명

        // 실행
        teamService.changeTeam(1L, new TeamChangeRequestDto(1L));

        // 검증
        assertEquals(Team.BLUE, testUserRoom.getTeam(), "팀 변경이 BLUE로 이루어졌는지 확인");
    }

    @Test
    void changeTeam_Success_BLUE_to_RED() {
        // 🔵 기존 팀이 BLUE → 🔴 RED로 변경
        testUserRoom = UserRoom.builder()
                .user(testUser)
                .room(testRoom)
                .team(Team.BLUE) // 초기 팀 BLUE
                .build();

        // Mock 설정
        when(roomRepository.findById(1L)).thenReturn(Optional.of(testRoom));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRoomRepository.findByUserAndRoom(testUser, testRoom)).thenReturn(Optional.of(testUserRoom));
        when(userRoomRepository.countByRoomAndTeam(testRoom, Team.RED)).thenReturn(0L); // RED 팀원이 0명

        // 실행
        teamService.changeTeam(1L, new TeamChangeRequestDto(1L));

        // 검증
        assertEquals(Team.RED, testUserRoom.getTeam(), "팀 변경이 RED로 이루어졌는지 확인");
    }
}
