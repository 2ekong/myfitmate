package com.myfitmate.myfitmate.domain.user.service;

import com.myfitmate.myfitmate.domain.user.entity.Token;
import com.myfitmate.myfitmate.domain.user.entity.User;
import com.myfitmate.myfitmate.domain.user.repository.TokenRepository;
import com.myfitmate.myfitmate.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public void deleteRefreshToken(Long userId) {
        tokenRepository.deleteByUser_Id(userId);
    }

    public String refreshAccessToken(String refreshToken) {
        // 🔍 토큰 파싱해서 userId 추출
        Long userId = jwtUtil.extractUserId(refreshToken);

        // 🔐 DB에서 refresh token 일치 여부 확인
        Token token = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰"));

        // 🕒 만료 확인
        if (token.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("리프레시 토큰이 만료되었습니다.");
        }

        // 🙆‍♂️ 새 access token 발급
        User user = token.getUser();
        return jwtUtil.createToken(user.getId(), user.getUsername());
    }

}
