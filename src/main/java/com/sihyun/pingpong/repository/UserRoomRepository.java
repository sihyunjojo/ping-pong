package com.sihyun.pingpong.repository;

import com.sihyun.pingpong.domain.Room;
import com.sihyun.pingpong.domain.User;
import com.sihyun.pingpong.domain.UserRoom;
import com.sihyun.pingpong.domain.enums.Team;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRoomRepository extends JpaRepository<UserRoom, Long> {
        boolean existsByUser(User user);
        long countByRoomAndTeam(Room room, Team team);
        long countByRoom(Room room);
        Optional<UserRoom> findByUserAndRoom(User user, Room room);
        void deleteByRoom(Room room);

        @Modifying // 기본적으로 @Query는 조회(SELECT) 쿼리만 실행할 수 있습니다.
        @Query("DELETE FROM UserRoom ur WHERE ur.room.id = :roomId")
        void deleteByRoomId(Long roomId);
}
