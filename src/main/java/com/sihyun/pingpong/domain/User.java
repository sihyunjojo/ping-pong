package com.sihyun.pingpong.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "member")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 사용자 ID

    private Integer fakerId; // Faker ID

    private String name; // 사용자 이름

//    @Column(unique = true, nullable = false)
    private String email; // 이메일

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status; // WAIT(대기), ACTIVE(활성), NON_ACTIVE(비활성)

    public enum UserStatus {
        WAIT, ACTIVE, NON_ACTIVE
    }
}
