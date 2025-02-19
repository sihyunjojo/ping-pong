package com.sihyun.pingpong.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

public enum RoomType {
    SINGLE(2, "SINGLE"),
    DOUBLE(4, "DOUBLE");

    private final int maxPlayers;
    private final String value;

    RoomType(int maxPlayers, String value) {
        this.maxPlayers = maxPlayers;
        this.value = value;
    }

    // ✅ 최대 플레이어 수 반환
    public int getMaxPlayers() {
        return maxPlayers;
    }

    // ✅ JSON 직렬화 시 사용될 값 반환 (예: "SINGLE", "DOUBLE")
    @JsonValue
    public String getValue() {
        return value;
    }

    // ✅ JSON 요청 값(String) → Enum 변환 (역직렬화)
    @JsonCreator
    public static RoomType from(String value) {
        return Stream.of(RoomType.values())
                .filter(type -> type.value.equalsIgnoreCase(value))
                .findFirst()
                .orElse(SINGLE); // 기본값: SINGLE
    }
}
