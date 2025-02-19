package com.sihyun.pingpong.service;

import com.sihyun.pingpong.domain.Room;
import com.sihyun.pingpong.domain.User;
import com.sihyun.pingpong.domain.enums.RoomStatus;
import com.sihyun.pingpong.dto.game.GameStartRequestDto;
import com.sihyun.pingpong.repository.RoomRepository;
import com.sihyun.pingpong.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class GameService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Transactional
    public void startGame(Long roomId, GameStartRequestDto request) {
        // 1. 방 & 유저 존재 여부 확인
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 방입니다."));
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));

        // 2. 방장이 맞는지 확인
        if (!room.getHost().equals(user)) {
            throw new IllegalStateException("호스트만 게임을 시작할 수 있습니다.");
        }

        // 3. 방 상태 확인 (WAIT 상태에서만 시작 가능)
        if (room.getStatus() != RoomStatus.WAIT) {
            throw new IllegalStateException("대기 상태인 방만 게임을 시작할 수 있습니다.");
        }

        // 4. 방 정원이 꽉 찼는지 확인
        long maxCapacity = room.getRoomType().getMaxPlayers(); // ✅ ENUM에서 직접 가져오기
        long currentPlayers = room.getUserRooms().size();
        if (currentPlayers < maxCapacity) {
            throw new IllegalStateException("방 정원이 가득 차야 게임을 시작할 수 있습니다.");
        }

        // 5. 방 상태를 PROGRESS로 변경
        room.setStatus(RoomStatus.PROGRESS);
        roomRepository.save(room);

        // 6. 1분 후 자동 게임 종료
        scheduleGameEnd(room);
    }

    private void scheduleGameEnd(Room room) {
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(60_000); // 1분 대기
                room.setStatus(RoomStatus.FINISH);
                roomRepository.save(room);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}
