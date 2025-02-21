package com.sihyun.pingpong.domain.enums;

public enum UserStatus {
    ACTIVE,      // 활성 (fakerId ≤ 30)
    WAIT,        // 대기 (31 ≤ fakerId ≤ 60)
    NON_ACTIVE   // 비활성 (fakerId ≥ 61)
}