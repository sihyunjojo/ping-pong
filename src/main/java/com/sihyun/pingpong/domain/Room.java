package com.sihyun.pingpong.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

import com.sihyun.pingpong.domain.enums.RoomType;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 방 ID

    private String title; // 방 제목

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private User host; // 방을 만든 User (방장)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType; // SINGLE(단식), DOUBLE(복식)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status; // WAIT(대기), PROGRESS(진행중), FINISH(완료)

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRoom> userRooms; // 방에 속한 유저 리스트
    
    public enum RoomStatus {
        WAIT, PROGRESS, FINISH
    }
}

