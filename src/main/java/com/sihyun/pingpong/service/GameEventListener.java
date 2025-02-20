package com.sihyun.pingpong.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
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
