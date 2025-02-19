package com.sihyun.pingpong.service;

import com.sihyun.pingpong.domain.Room;
import com.sihyun.pingpong.domain.User;
import com.sihyun.pingpong.domain.UserRoom;
import com.sihyun.pingpong.domain.enums.RoomStatus;
import com.sihyun.pingpong.domain.enums.RoomType;
import com.sihyun.pingpong.dto.room.RoomCreateRequestDto;
import com.sihyun.pingpong.dto.room.RoomDetailResponseDto;
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
}