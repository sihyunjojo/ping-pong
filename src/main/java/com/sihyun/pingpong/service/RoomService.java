package com.sihyun.pingpong.service;

import com.sihyun.pingpong.domain.Room;
import com.sihyun.pingpong.domain.User;
import com.sihyun.pingpong.domain.UserRoom;
import com.sihyun.pingpong.domain.enums.RoomStatus;
import com.sihyun.pingpong.domain.enums.RoomType;
import com.sihyun.pingpong.dto.room.RoomCreateRequestDto;
import com.sihyun.pingpong.dto.room.RoomDetailResponseDto;
import com.sihyun.pingpong.dto.room.RoomJoinRequestDto;
import com.sihyun.pingpong.dto.room.RoomLeaveRequestDto;
import com.sihyun.pingpong.dto.room.RoomListResponseDto;
import com.sihyun.pingpong.dto.room.RoomResponseDto;
import com.sihyun.pingpong.repository.RoomRepository;
import com.sihyun.pingpong.repository.UserRepository;
import com.sihyun.pingpong.repository.UserRoomRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final UserRoomRepository userRoomRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public void createRoom(RoomCreateRequestDto request) {
        // 1. 유저가 존재하는지 확인
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // 2. 유저 상태 확인 (ACTIVE 상태인지)
        if (user.getStatus() != User.UserStatus.ACTIVE) {
            throw new IllegalStateException("방을 생성할 수 없는 유저 상태입니다.");
        }

        // 3. 유저가 이미 참여한 방이 있는지 확인
        if (userRoomRepository.existsByUser(user)) {
            throw new IllegalStateException("이미 참여한 방이 있습니다.");
        }

        // 4. 방 생성 및 저장
        Room room = Room.builder()
                .title(request.title())
                .roomType(request.roomType() != null ? request.roomType() : RoomType.SINGLE) // ✅ 기본값 설정
                .host(user)
                .status(RoomStatus.WAIT) // WAIT 상태로 생성
                .build();

        roomRepository.save(room);
        
        // 5. 방 생성한 사람을 자동으로 참가시키기
        userRoomRepository.save(
            UserRoom.builder()
                .user(user)
                .room(room)
                .team(assignTeam(room)) // 팀 배정 로직 추가
                .build()
        );
    }

    @Transactional(readOnly = true)
    public RoomListResponseDto getAllRoomsWithPaging(int page, int size) {
        Page<Room> roomPage = roomRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
        List<RoomResponseDto> roomList = roomPage.getContent().stream()
                .map(RoomResponseDto::fromEntity)
                .toList();

        return new RoomListResponseDto(
                roomPage.getTotalElements(),
                roomPage.getTotalPages(),
                roomList
        );
    }

    @Transactional(readOnly = true)
    public RoomDetailResponseDto getRoomDetail(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 방입니다."));

        return new RoomDetailResponseDto(
                room.getId(),
                room.getTitle(),
                room.getHost().getId(),
                room.getRoomType().name(),
                room.getStatus().name(),
                room.getCreatedAt().format(FORMATTER),
                room.getUpdatedAt().format(FORMATTER)
        );
    }

    @Transactional
    public void joinRoom(Long roomId, RoomJoinRequestDto request) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 방입니다."));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));

        // 방 상태 확인
        if (room.getStatus() != RoomStatus.WAIT) {
            throw new IllegalStateException("대기 상태인 방만 참가할 수 있습니다.");
        }

        // 유저 상태 확인
        if (user.getStatus() != User.UserStatus.ACTIVE) {
            throw new IllegalStateException("활성 상태인 유저만 참가할 수 있습니다.");
        }

        // 유저가 이미 다른 방에 참여 중인지 확인
        if (userRoomRepository.existsByUser(user)) {
            throw new IllegalStateException("유저는 이미 다른 방에 참가 중입니다.");
        }

        // 방 정원 확인
        long currentMemberCount = userRoomRepository.countByRoom(room);
        long maxCapacity = room.getRoomType() == RoomType.SINGLE ? 2 : 4;
        if (currentMemberCount >= maxCapacity) {
            throw new IllegalStateException("방이 가득 차 있어 참가할 수 없습니다.");
        }

        // 팀 배정 로직
        UserRoom.Team team = assignTeam(room);

        // 유저를 방에 추가
        userRoomRepository.save(
            UserRoom.builder()
                .user(user)
                .room(room)
                .team(team)
                .build()
        );
    }

    private UserRoom.Team assignTeam(Room room) {
        long redCount = userRoomRepository.countByRoomAndTeam(room, UserRoom.Team.RED);
        long blueCount = userRoomRepository.countByRoomAndTeam(room, UserRoom.Team.BLUE);

        if (redCount > blueCount) {
            return UserRoom.Team.BLUE;
        } else {
            return UserRoom.Team.RED;
        }
    }


    @Transactional
    public void leaveRoom(Long roomId, RoomLeaveRequestDto request) {
        // 1. 방 & 유저 존재 여부 확인
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        // 2. 유저가 방에 참가한 상태인지 확인
        UserRoom userRoom = userRoomRepository.findByUserAndRoom(user, room)
                .orElseThrow(() -> new IllegalStateException("유저가 해당 방에 참가하지 않았습니다."));

        // 3. 이미 진행(PROGRESS) 또는 종료(FINISH) 상태인 방인지 확인
        if (room.getStatus() == RoomStatus.PROGRESS || room.getStatus() == RoomStatus.FINISH) {
            throw new IllegalStateException("게임이 진행 중이거나 이미 종료된 방에서는 나갈 수 없습니다.");
        }

        // 4. 방을 나가는 로직
        userRoomRepository.delete(userRoom);

        // 5. 만약 방장이 나가면 방을 종료 상태(FINISH)로 변경
        if (room.getHost().equals(user)) {
            room.setStatus(RoomStatus.FINISH);
        }
    }
}