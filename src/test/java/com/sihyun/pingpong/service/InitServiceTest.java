package com.sihyun.pingpong.service;

import com.sihyun.pingpong.domain.enums.UserStatus;
import com.sihyun.pingpong.repository.RoomRepository;
import com.sihyun.pingpong.repository.UserRepository;
import com.sihyun.pingpong.repository.UserRoomRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class InitServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRoomRepository userRoomRepository;

    @InjectMocks
    private InitService initService; // ✅ @Mock 객체들이 자동 주입됨


    @Test
    void testDetermineStatus_Active() {
        // ✅ fakerId가 10이면 ACTIVE여야 함
        UserStatus status = initService.determineStatus(10);
        assertEquals(UserStatus.ACTIVE, status);
    }

    @Test
    void testDetermineStatus_Wait() {
        // ✅ fakerId가 40이면 WAIT여야 함
        UserStatus status = initService.determineStatus(40);
        assertEquals(UserStatus.WAIT, status);
    }

    @Test
    void testDetermineStatus_NonActive() {
        // ✅ fakerId가 80이면 NON_ACTIVE여야 함
        UserStatus status = initService.determineStatus(80);
        assertEquals(UserStatus.NON_ACTIVE, status);
    }
}
