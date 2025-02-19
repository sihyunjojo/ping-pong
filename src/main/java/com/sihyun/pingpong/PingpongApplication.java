package com.sihyun.pingpong;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.sihyun.pingpong.dto.init.InitRequestDto;
import com.sihyun.pingpong.service.InitService;

@SpringBootApplication
public class PingpongApplication {

	public static void main(String[] args) {
		SpringApplication.run(PingpongApplication.class, args);
	}

	// @Bean
	// public CommandLineRunner initData(InitService initService) {
	// 	return args -> {
	// 		initService.initializeDatabase(new InitRequestDto(1, 10));
	// 	};
	// }
}
