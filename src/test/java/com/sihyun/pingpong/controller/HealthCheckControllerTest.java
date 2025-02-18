package com.sihyun.pingpong.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HealthCheckControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context; 

    @BeforeEach
    void setup() {
        // @WebMvcTest 대신 @SpringBootTest를 사용하면 Spring 컨텍스트 전체를 로드하므로 MockMvc를 수동으로 설정하는 것이 유리함.
        // 만약 여러 개의 테스트가 실행될 경우, MockMvc를 재사용하면 이전 테스트의 영향이 다음 테스트에 남을 가능성이 있음.
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("헬스체크 API 테스트")
    void healthCheckTest() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("API 요청이 성공했습니다."));
    }
}
