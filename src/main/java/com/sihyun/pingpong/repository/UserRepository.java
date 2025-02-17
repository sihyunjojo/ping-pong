package com.sihyun.pingpong.repository;

import com.sihyun.pingpong.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
