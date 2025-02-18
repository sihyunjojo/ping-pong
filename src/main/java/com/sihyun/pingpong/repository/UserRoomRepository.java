package com.sihyun.pingpong.repository;

import com.sihyun.pingpong.domain.User;
import com.sihyun.pingpong.domain.UserRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoomRepository extends JpaRepository<UserRoom, Long> {
        boolean existsByUser(User user);
}
