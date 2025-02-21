package com.sihyun.pingpong.service;

import com.sihyun.pingpong.domain.User;
import com.sihyun.pingpong.domain.enums.UserStatus;
import com.sihyun.pingpong.dto.init.InitRequestDto;
import com.sihyun.pingpong.repository.RoomRepository;
import com.sihyun.pingpong.repository.UserRepository;
import com.sihyun.pingpong.repository.UserRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final UserRoomRepository userRoomRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    // private static final String FAKER_API_URL = "https://fakerapi.it/api/v1/users?_seed=%d&_quantity=%d&_locale=ko_KR";
    private static final String FAKER_API_URL = "http://localhost:8080/fake-api/users?_seed=%d&_quantity=%d&_locale=ko_KR";

    @Transactional
    public void initializeDatabase(InitRequestDto request) {
        // 1. 모든 데이터 삭제
        cleanup();

          // 2. Faker API 호출
        String url = String.format(FAKER_API_URL, request.seed(), request.quantity());

        // ---- HTTP 헤더 추가 ----
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON)); // JSON 응답을 기대

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        // 3. 응답 데이터 처리
        Map<String, Object> response = responseEntity.getBody();
        if (response == null || !response.containsKey("data")) {
            throw new RuntimeException("API 응답이 올바르지 않습니다: " + response);
        }

        List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");

        // 4. 유저 데이터 변환 및 저장
        List<User> users = data.stream()
                .map(user -> User.builder()
                        .fakerId((Integer) user.get("id"))
                        .name((String) user.get("username"))
                        .email((String) user.get("email"))
                        .status(determineStatus((Integer) user.get("id")))
                        .build())
                .sorted((u1, u2) -> Integer.compare(u1.getFakerId(), u2.getFakerId())) // fakerId 기준 정렬
                .toList();

        userRepository.saveAll(users);
    }

    UserStatus determineStatus(int fakerId) {
        if (fakerId <= 30) return UserStatus.ACTIVE;
        if (fakerId <= 60) return UserStatus.WAIT;
        return UserStatus.NON_ACTIVE;
    }

    public void cleanup() {
        userRepository.deleteAll();
        roomRepository.deleteAll();
        userRoomRepository.deleteAll();
    }
}
