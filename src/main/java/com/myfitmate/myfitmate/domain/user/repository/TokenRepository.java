package com.myfitmate.myfitmate.domain.user.repository;

import com.myfitmate.myfitmate.domain.user.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    // 유저 ID로 토큰 삭제 (회원 탈퇴 시 같이 삭제)
    void deleteByUser_Id(Long userId);

    // 리프레시 토큰으로 찾기
    Optional<Token> findByRefreshToken(String refreshToken);

    // 유저 ID로 토큰 찾기
    Optional<Token> findByUser_Id(Long userId);

    // 유저 ID + 리프레시 토큰 존재 확인
    boolean existsByUser_IdAndRefreshToken(Long userId, String refreshToken);

    // 리프레시 토큰 갱신
    @Modifying
    @Query("UPDATE Token t SET t.refreshToken = :refreshToken, t.expiredAt = :expiredAt WHERE t.user.id = :userId")
    void updateRefreshToken(@Param("userId") Long userId,
                            @Param("refreshToken") String refreshToken,
                            @Param("expiredAt") LocalDateTime expiredAt);
}
