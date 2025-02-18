package com.sihyun.pingpong.service;

import com.sihyun.pingpong.domain.Room;
import com.sihyun.pingpong.domain.User;
import com.sihyun.pingpong.domain.enums.RoomType;
import com.sihyun.pingpong.dto.room.RoomCreateRequestDto;
import com.sihyun.pingpong.repository.RoomRepository;
import com.sihyun.pingpong.repository.UserRepository;
import com.sihyun.pingpong.repository.UserRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final UserRoomRepository userRoomRepository;

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
                .status(Room.RoomStatus.WAIT) // WAIT 상태로 생성
                .build();

        roomRepository.save(room);
    }
}
