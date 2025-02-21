package com.sihyun.pingpong.event.listener;

import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import com.sihyun.pingpong.event.GameStartedEvent;
import com.sihyun.pingpong.service.GameService;

import org.springframework.transaction.event.TransactionPhase;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GameEventListener {

    private final GameService gameService;

    public GameEventListener(GameService gameService) {
        this.gameService = gameService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onGameStart(GameStartedEvent event) {
        gameService.scheduleGameEnd(event.getRoomId());
    }
}
