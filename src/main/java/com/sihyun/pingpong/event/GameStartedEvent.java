package com.sihyun.pingpong.event;

import lombok.Getter;

@Getter
public class GameStartedEvent {
    private final Long roomId;

    public GameStartedEvent(Long roomId) {
        this.roomId = roomId;
    }
}
