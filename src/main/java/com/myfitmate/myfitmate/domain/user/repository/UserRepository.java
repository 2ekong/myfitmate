package com.myfitmate.myfitmate.domain.user.repository;

import com.myfitmate.myfitmate.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByKakaoId(String kakaoId);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
