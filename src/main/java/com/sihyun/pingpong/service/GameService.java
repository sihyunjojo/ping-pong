package com.sihyun.pingpong.service;

import com.sihyun.pingpong.domain.Room;
import com.sihyun.pingpong.domain.User;
import com.sihyun.pingpong.domain.enums.RoomStatus;
import com.sihyun.pingpong.dto.game.GameStartRequestDto;
import com.sihyun.pingpong.event.GameStartedEvent;
import com.sihyun.pingpong.exception.GameServiceException;
import com.sihyun.pingpong.repository.RoomRepository;
import com.sihyun.pingpong.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final EndGameService endGameService;

    private final ApplicationEventPublisher eventPublisher;

    @PersistenceContext
    private EntityManager entityManager; // 👈 EntityManager 주입

    @Transactional
    public void startGame(Long roomId, GameStartRequestDto request) {
        // 1. 방 & 유저 존재 여부 확인
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new GameServiceException("존재하지 않는 방입니다."));
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new GameServiceException("존재하지 않는 유저입니다."));

        // 2. 방장이 맞는지 확인
        if (!room.getHost().equals(user)) {
            throw new GameServiceException("호스트만 게임을 시작할 수 있습니다.");
        }

        // 3. 방 상태 확인 (WAIT 상태에서만 시작 가능)
        if (room.getStatus() != RoomStatus.WAIT) {
            throw new GameServiceException("대기 상태인 방만 게임을 시작할 수 있습니다.");
        }

        // 4. 방 정원이 꽉 찼는지 확인
        long maxCapacity = room.getRoomType().getMaxPlayers(); // ✅ ENUM에서 직접 가져오기
        long currentPlayers = room.getUserRooms().size();
        if (currentPlayers < maxCapacity) {
            throw new GameServiceException("방 정원이 가득 차야 게임을 시작할 수 있습니다.");
        }

        // 5. 방 상태를 PROGRESS로 변경
        room.setStatus(RoomStatus.PROGRESS);
        roomRepository.save(room);
        // entityManager.flush(); // 🚀 변경 사항 즉시 반영

        // 6. 1분 후 자동 게임 종료
        // scheduleGameEnd(roomId);

          // 비동기로 처리하기 위해서 Event Publisher 사용
          eventPublisher.publishEvent(new GameStartedEvent(roomId));
    }

@Async
    public void scheduleGameEnd(Long roomId) {
        try {
            Thread.sleep(60_000);
            endGameService.endGameTransactional(roomId); // 새 트랜잭션을 사용하기 위해 EndGameService 사용
            // endGameTransactional(roomId); // 트랜잭션이 포함된 메서드 호출
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        } finally {
            log.info("1분 지남");
        }
    }
    
}