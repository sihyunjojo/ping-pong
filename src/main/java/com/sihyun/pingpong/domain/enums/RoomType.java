package com.sihyun.pingpong.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RoomType {
    SINGLE("SINGLE"),
    DOUBLE("DOUBLE");

    private final String value;

    RoomType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static RoomType from(String value) {
        for (RoomType type : RoomType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return SINGLE;
    }
}
