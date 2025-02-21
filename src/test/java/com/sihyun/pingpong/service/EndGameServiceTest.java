package com.sihyun.pingpong.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.sihyun.pingpong.domain.Room;
import com.sihyun.pingpong.domain.enums.RoomStatus;
import com.sihyun.pingpong.exception.GameServiceException;
import com.sihyun.pingpong.repository.RoomRepository;
import com.sihyun.pingpong.repository.UserRoomRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class EndGameServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRoomRepository userRoomRepository;

    @InjectMocks
    private EndGameService endGameService;

    private Room mockRoom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockRoom = Room.builder()
                .id(1L)
                .status(RoomStatus.PROGRESS) // 진행 중인 방
                .build();
    }

    @Test
    void endGameTransactional_Success() {
        // Given
        when(roomRepository.findById(mockRoom.getId())).thenReturn(Optional.of(mockRoom));

        // When
        endGameService.endGameTransactional(mockRoom.getId());

        // Then
        verify(userRoomRepository, times(1)).deleteByRoom(mockRoom);
        verify(roomRepository, times(1)).save(mockRoom);
        assertEquals(RoomStatus.FINISH, mockRoom.getStatus(), "게임 종료 후 방 상태는 FINISH여야 함");
    }

    @Test
    void endGameTransactional_Fail_RoomNotFound() {
        // Given
        Long invalidRoomId = 99L;
        when(roomRepository.findById(invalidRoomId)).thenReturn(Optional.empty());

        // When & Then
        GameServiceException exception = assertThrows(GameServiceException.class, () -> 
            endGameService.endGameTransactional(invalidRoomId)
        );

        assertEquals("존재하지 않는 방입니다.", exception.getMessage());
        verify(userRoomRepository, never()).deleteByRoom(any());
        verify(roomRepository, never()).save(any());
    }
}
