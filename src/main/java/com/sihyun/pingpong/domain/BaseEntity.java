package com.sihyun.pingpong.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.Getter;

@MappedSuperclass // 공통 필드를 상속받도록 설정
@Getter
public class BaseEntity {

    @Column(updatable = false) // 생성 시간은 변경 불가
    private LocalDateTime createdAt; // 생성일시

    private LocalDateTime updatedAt; // 수정일시

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
