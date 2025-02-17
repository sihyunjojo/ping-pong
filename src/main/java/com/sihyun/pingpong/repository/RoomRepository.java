package com.sihyun.pingpong.repository;

import com.sihyun.pingpong.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
