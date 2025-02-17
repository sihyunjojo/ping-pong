package com.sihyun.pingpong.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "\uD83C\uDFD3 탁구 게임 API",
                description = """
                            이 API는 탁구 게임을 위한 RESTful API입니다.
                            사용자는 방을 생성하고 참가하며, 단식(1v1)과 복식(2v2) 게임을 진행할 수 있습니다.
                        """,
                version = "v1",
                contact = @Contact(
                        name = "조시현",
                        email = "si4018@naver.com",
                        url = "https://github.com/sihyunjojo/ping-pong"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "로컬 개발 서버")
        },
        tags = {
                @Tag(name = "Health", description = "서버 상태 체크 API"),
//                @Tag(name = "User", description = "유저 관련 API"),
//                @Tag(name = "Room", description = "게임 방 관련 API"),
//                @Tag(name = "Game", description = "게임 진행 관련 API")
        }
)
public class SwaggerConfiguration {

}
