package com.sihyun.pingpong.service;

import com.sihyun.pingpong.dto.team.TeamChangeRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sihyun.pingpong.domain.Room;
import com.sihyun.pingpong.domain.User;
import com.sihyun.pingpong.domain.UserRoom;
import com.sihyun.pingpong.domain.enums.RoomStatus;
import com.sihyun.pingpong.domain.enums.Team;
import com.sihyun.pingpong.exception.RoomServiceException;
import com.sihyun.pingpong.repository.RoomRepository;
import com.sihyun.pingpong.repository.UserRepository;
import com.sihyun.pingpong.repository.UserRoomRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TeamService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final UserRoomRepository userRoomRepository;

    @Transactional
    public void changeTeam(Long roomId, TeamChangeRequestDto request) {
        // 1. 방 및 유저 조회
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomServiceException("존재하지 않는 방입니다."));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RoomServiceException("존재하지 않는 유저입니다."));

        // 2. 유저가 해당 방에 참가하고 있는지 확인
        UserRoom userRoom = userRoomRepository.findByUserAndRoom(user, room)
                .orElseThrow(() -> new RoomServiceException("유저가 해당 방에 참가하지 않았습니다."));

        // 3. 방의 상태 확인 (대기 상태인지)
        if (room.getStatus() != RoomStatus.WAIT) {
            throw new RoomServiceException("대기 상태에서만 팀을 변경할 수 있습니다.");
        }

        // 4. 현재 팀과 반대 팀 구하기
        Team currentTeam = userRoom.getTeam();
        Team oppositeTeam = (currentTeam == Team.RED) ? Team.BLUE : Team.RED;

        // 5. 반대 팀에 인원이 정원의 절반을 초과하면 변경 불가능
        long teamSize = userRoomRepository.countByRoomAndTeam(room, oppositeTeam);
        long maxTeamSize = room.getRoomType().getMaxPlayers() / 2; // 단식: 1명, 복식: 2명

        if (teamSize >= maxTeamSize) {
            throw new RoomServiceException("상대 팀이 정원의 절반만큼 찼기 때문에 변경할 수 없습니다.");
        }

        // 6. 팀 변경 적용
        userRoom.setTeam(oppositeTeam);
    }
    
}
