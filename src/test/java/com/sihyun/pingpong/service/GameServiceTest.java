package com.sihyun.pingpong.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.sihyun.pingpong.domain.Room;
import com.sihyun.pingpong.domain.User;
import com.sihyun.pingpong.domain.UserRoom;
import com.sihyun.pingpong.domain.enums.RoomStatus;
import com.sihyun.pingpong.domain.enums.RoomType;
import com.sihyun.pingpong.domain.enums.Team;
import com.sihyun.pingpong.dto.game.GameStartRequestDto;
import com.sihyun.pingpong.event.GameStartedEvent;
import com.sihyun.pingpong.repository.RoomRepository;
import com.sihyun.pingpong.repository.UserRepository;
import com.sihyun.pingpong.repository.UserRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoomRepository userRoomRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private GameService gameService;

    private Room room;
    private User hostUser;
    private User user1;
    private User user2;
    private List<UserRoom> userRooms; // ✅ 실제 UserRoom 리스트

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // ✅ 테스트용 유저 생성
        hostUser = User.builder().id(1L).fakerId(1001).name("HostUser").email("host@example.com").status(null).build();
        user1 = User.builder().id(2L).fakerId(1002).name("User1").email("user1@example.com").status(null).build();

        // ✅ 방 생성 (UserRoom 포함)
        userRooms = new ArrayList<>();
        room = Room.builder()
                .id(1L)
                .host(hostUser)
                .status(RoomStatus.WAIT)
                .roomType(RoomType.SINGLE)
                .userRooms(userRooms) // ✅ 리스트 추가
                .build();

        // ✅ UserRoom 객체 추가
        userRooms.add(UserRoom.builder().user(user1).room(room).team(Team.RED).build());
        userRooms.add(UserRoom.builder().user(user2).room(room).team(Team.BLUE).build());

        // ✅ Mock 설정
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(userRepository.findById(1L)).thenReturn(Optional.of(hostUser));
    }


    @Test
    @DisplayName("startGame() 메서드가 정상 동작하는 경우 테스트")
    public void testStartGame_Success() {
        // ✅ 요청 DTO 생성
        GameStartRequestDto requestDto = new GameStartRequestDto(1L);

        // ✅ 실행 (이제 실제 2명의 userRoom 포함)
        gameService.startGame(1L, requestDto);

        // ✅ 검증
        assertEquals(RoomStatus.PROGRESS, room.getStatus()); // 상태 변경 확인
        verify(roomRepository, times(1)).save(room); // 저장 로직 확인

        // ✅ 이벤트 캡처 및 추가 검증 (방법 2)
        ArgumentCaptor<GameStartedEvent> eventCaptor = ArgumentCaptor.forClass(GameStartedEvent.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());
        assertEquals(1L, eventCaptor.getValue().getRoomId()); // 발생한 이벤트가 올바른 roomId인지 확인
    }

}
