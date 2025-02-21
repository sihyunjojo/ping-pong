package com.sihyun.pingpong.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.sihyun.pingpong.domain.Room;
import com.sihyun.pingpong.domain.User;
import com.sihyun.pingpong.domain.UserRoom;
import com.sihyun.pingpong.domain.enums.RoomStatus;
import com.sihyun.pingpong.domain.enums.RoomType;
import com.sihyun.pingpong.domain.enums.Team;
import com.sihyun.pingpong.domain.enums.UserStatus;
import com.sihyun.pingpong.dto.room.RoomCreateRequestDto;
import com.sihyun.pingpong.dto.room.RoomJoinRequestDto;
import com.sihyun.pingpong.dto.room.RoomLeaveRequestDto;
import com.sihyun.pingpong.dto.room.RoomDetailResponseDto;
import com.sihyun.pingpong.dto.room.RoomListResponseDto;
import com.sihyun.pingpong.repository.RoomRepository;
import com.sihyun.pingpong.repository.UserRepository;
import com.sihyun.pingpong.repository.UserRoomRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoomRepository userRoomRepository;

    @InjectMocks
    private RoomService roomService;

    private User mockUser;
    private User normalUser;
    private Room mockRoom;
    private Room mockDoubleRoom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        mockUser = User.builder()
                .id(1L)
                .status(UserStatus.ACTIVE)
                .build();
        
        // ✅ 방장과 다른 일반 유저 추가
        normalUser = User.builder()
            .id(2L) // ✅ 방장이 아닌 유저
            .status(UserStatus.ACTIVE)
            .build();
        mockRoom = Room.builder()
                .id(1L)
                .title("Test Room")
                .roomType(RoomType.SINGLE)
                .host(mockUser)
                .status(RoomStatus.WAIT)
                .build();
        mockDoubleRoom = Room.builder()
                .id(1L)
                .title("Test Room")
                .roomType(RoomType.DOUBLE)
                .host(mockUser)
                .status(RoomStatus.WAIT)
                .build();

        // ✅ ReflectionTestUtils를 사용하여 private 필드 값 강제 설정
        ReflectionTestUtils.setField(mockRoom, "createdAt", LocalDateTime.now());
        ReflectionTestUtils.setField(mockRoom, "updatedAt", LocalDateTime.now()); 
    }

    @Test
    void createRoom_Success() {
        // Given
        RoomCreateRequestDto request = new RoomCreateRequestDto(mockUser.getId(), RoomType.SINGLE, "Test Room");
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        when(userRoomRepository.existsByUser(mockUser)).thenReturn(false);
        when(roomRepository.save(any(Room.class))).thenReturn(mockRoom);

        // When
        roomService.createRoom(request);

        // Then
        verify(roomRepository, times(1)).save(any(Room.class));
        verify(userRoomRepository, times(1)).save(any(UserRoom.class));
    }

    @ParameterizedTest
    @CsvSource({"1, 1, BLUE", "2, 2, RED"})
    void joinRoom_Success(int redCount, int blueCount, Team expectedTeam) {
        // Given
        RoomJoinRequestDto request = new RoomJoinRequestDto(mockUser.getId());
        when(roomRepository.findById(mockDoubleRoom.getId())).thenReturn(Optional.of(mockDoubleRoom));
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        when(userRoomRepository.existsByUser(mockUser)).thenReturn(false);
        when(userRoomRepository.countByRoom(mockDoubleRoom)).thenReturn(3L); // 방에 3명 있음
        when(userRoomRepository.countByRoomAndTeam(mockDoubleRoom, Team.RED)).thenReturn((long) redCount);
        when(userRoomRepository.countByRoomAndTeam(mockDoubleRoom, Team.BLUE)).thenReturn((long) blueCount);

        // When
        roomService.joinRoom(mockDoubleRoom.getId(), request);

        // Then        verify(userRoomRepository, times(1)).save(argThat(userRoom -> userRoom.getTeam() == expectedTeam));
    }

    @Test
    void leaveRoom_Success_NormalUser() {
        // Given
        RoomLeaveRequestDto request = new RoomLeaveRequestDto(normalUser.getId()); // ✅ 일반 유저가 방을 나감
        UserRoom userRoom = UserRoom.builder().user(normalUser).room(mockRoom).team(Team.RED).build();
    
        when(roomRepository.findById(mockRoom.getId())).thenReturn(Optional.of(mockRoom));
        when(userRepository.findById(normalUser.getId())).thenReturn(Optional.of(normalUser));
        when(userRoomRepository.findByUserAndRoom(normalUser, mockRoom)).thenReturn(Optional.of(userRoom));
    
        // When
        roomService.leaveRoom(mockRoom.getId(), request);
    
        // ✅ 일반 유저만 삭제되었는지 검증
        verify(userRoomRepository, times(1)).delete(userRoom);
        verify(userRoomRepository, never()).deleteByRoom(mockRoom); // ✅ 방 전체 삭제가 실행되지 않았는지 확인
    }

    @Test
    void leaveRoom_Success_Host() {
        // Given
        RoomLeaveRequestDto request = new RoomLeaveRequestDto(mockUser.getId());

        when(roomRepository.findById(mockRoom.getId())).thenReturn(Optional.of(mockRoom));
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        when(userRoomRepository.findByUserAndRoom(mockUser, mockRoom)).thenReturn(Optional.of(
                UserRoom.builder().user(mockUser).room(mockRoom).team(Team.RED).build()
        ));

        // When
        roomService.leaveRoom(mockRoom.getId(), request);

        // Then
        verify(userRoomRepository, times(1)).deleteByRoom(mockRoom);
        assertEquals(RoomStatus.FINISH, mockRoom.getStatus(), "방장이 나가면 방은 FINISH 상태가 되어야 함");
    }

    @Test
    void getRoomDetail_Success() {
        // Given
        when(roomRepository.findById(mockRoom.getId())).thenReturn(Optional.of(mockRoom));

        // When
        RoomDetailResponseDto response = roomService.getRoomDetail(mockRoom.getId());

        // Then
        assertNotNull(response);
        assertEquals(mockRoom.getId(), response.id());
        assertEquals(mockRoom.getTitle(), response.title());
    }

    @ParameterizedTest
    @CsvSource({"0, 5", "1, 3", "2, 2"})
    void getAllRoomsWithPaging_Success(int page, int size) {
        // Given
        List<Room> rooms = List.of(mockRoom, mockRoom, mockRoom);
        Page<Room> roomPage = new PageImpl<>(rooms, PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")), rooms.size());
        when(roomRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")))).thenReturn(roomPage);

        // When
        RoomListResponseDto response = roomService.getAllRoomsWithPaging(page, size);

        // Then
        assertNotNull(response);
        assertEquals(rooms.size(), response.roomList().size());
    }
}
