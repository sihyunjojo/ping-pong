package com.sihyun.pingpong.repository;

import com.sihyun.pingpong.domain.Room;
import com.sihyun.pingpong.domain.User;
import com.sihyun.pingpong.domain.UserRoom;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoomRepository extends JpaRepository<UserRoom, Long> {
        boolean existsByUser(User user);
        long countByRoomAndTeam(Room room, UserRoom.Team team);
        long countByRoom(Room room);
        Optional<UserRoom> findByUserAndRoom(User user, Room room);
}
