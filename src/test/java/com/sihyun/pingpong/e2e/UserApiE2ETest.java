package com.sihyun.pingpong.e2e;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Disabled
@TestMethodOrder(OrderAnnotation.class) // 실행 순서를 보장
public class UserApiE2ETest {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "http://localhost:8080"; // API 기본 URL 설정
    }

    @BeforeAll
    public static void cleanup() {
        RestAssured
            .given()
            .when()
                .delete("http://localhost:8080/cleanup") // DB 초기화 API 추가 필요
            .then()
                .statusCode(200);
    }


    @Test
    @Order(1) // 1️⃣ 헬스체크 테스트
    public void testHealthCheck() {
        given()
            .when().get("/health")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("code", equalTo(200))
            .body("message", equalTo("API 요청이 성공했습니다."));
    }

    @Test
    @Order(2) // 2️⃣ 유저 전체 조회 (초기 상태)
    public void testGetUsersBeforeInit() {
        given()
            .queryParam("size", 100)
            .queryParam("page", 0)
            .when().get("/user")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("code", equalTo(200))
            .body("message", equalTo("API 요청이 성공했습니다."))
            .body("result.totalElements", equalTo(0))
            .body("result.totalPages", equalTo(0))
            .body("result.userList", hasSize(0)); // 초기에는 유저가 없어야 함
    }

    @Test
    @Order(3) // 3️⃣ 초기화 API 호출
    public void testInitUsers() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"seed\":123, \"quantity\":10}")
            .when().post("/init")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("code", equalTo(200))
            .body("message", equalTo("API 요청이 성공했습니다."));
    }

    @Test
    @Order(4) // 4️⃣ 유저 전체 조회 (초기화 후)
    public void testGetUsersAfterInit() {
        given()
            .queryParam("size", 10)
            .queryParam("page", 0)
            .when().get("/user")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("code", equalTo(200))
            .body("message", equalTo("API 요청이 성공했습니다."))
            .body("result.totalElements", equalTo(10)) // 초기화 후 10명이 있어야 함
            .body("result.totalPages", equalTo(1))
            .body("result.userList", hasSize(10)) // 유저 리스트 크기 확인
            .body("result.userList[0].id", notNullValue()) // 유저 데이터 검증
            .body("result.userList[0].name", notNullValue())
            .body("result.userList[0].email", notNullValue())
            .body("result.userList[0].status", equalTo("ACTIVE"));
    }
}
