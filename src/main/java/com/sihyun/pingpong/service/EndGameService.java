package com.sihyun.pingpong.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import com.sihyun.pingpong.domain.Room;
import com.sihyun.pingpong.domain.enums.RoomStatus;
import com.sihyun.pingpong.repository.RoomRepository;
import com.sihyun.pingpong.repository.UserRoomRepository;
import com.sihyun.pingpong.exception.GameServiceException;

@Service
@Slf4j
@RequiredArgsConstructor
public class EndGameService {

    private final RoomRepository roomRepository;
    private final UserRoomRepository userRoomRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void endGameTransactional(Long roomId) {
        log.debug(roomId + "방 게임 끝" + LocalDateTime.now());

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new GameServiceException("존재하지 않는 방입니다."));

        try {
            userRoomRepository.deleteByRoom(room);
        } catch (Exception e) {
            log.error("❌ userRoom 삭제 중 예외 발생: {}", e.getMessage(), e);
        }

        room.setStatus(RoomStatus.FINISH);
        roomRepository.save(room);
    }
}
