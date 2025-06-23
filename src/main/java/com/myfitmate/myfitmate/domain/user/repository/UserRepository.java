package com.myfitmate.myfitmate.domain.user.repository;

import com.myfitmate.myfitmate.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByKakaoId(Long kakaoId);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    List<User> username(String username);


}
