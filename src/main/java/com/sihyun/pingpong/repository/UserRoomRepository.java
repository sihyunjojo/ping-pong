package com.sihyun.pingpong.repository;

import com.sihyun.pingpong.domain.UserRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoomRepository extends JpaRepository<UserRoom, Long> {
}
