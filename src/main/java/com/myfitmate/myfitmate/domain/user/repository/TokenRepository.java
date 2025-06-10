package com.myfitmate.myfitmate.domain.user.repository;

import com.myfitmate.myfitmate.domain.user.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    // 유저 ID로 토큰 삭제 (회원 탈퇴 시 같이 삭제)
    void deleteByUser_Id(Long userId);
    Optional<Token> findByRefreshToken(String refreshToken);

}
