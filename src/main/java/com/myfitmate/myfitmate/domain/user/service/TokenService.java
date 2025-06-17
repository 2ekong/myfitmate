package com.myfitmate.myfitmate.domain.user.service;

import com.myfitmate.myfitmate.domain.user.dto.TokenResponseDto;
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
        Long userId = jwtUtil.extractUserId(refreshToken);

        Token token = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰"));

        if (token.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("리프레시 토큰이 만료되었습니다.");
        }

        User user = token.getUser();
        return jwtUtil.createToken(user.getId(), user.getUsername());
    }

    @Transactional
    public TokenResponseDto refreshAllTokens(String refreshToken) {
        Long userId = jwtUtil.extractUserId(refreshToken);

        if (!tokenRepository.existsByUser_IdAndRefreshToken(userId, refreshToken)) {
            throw new RuntimeException("리프레시 토큰이 유효하지 않습니다.");
        }

        Token token = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 리프레시 토큰"));

        User user = token.getUser();

        String newAccessToken = jwtUtil.createAccessToken(user);
        String newRefreshToken = jwtUtil.createRefreshToken(user.getId());

        // updateRefreshToken → 직접 수정 방식으로 교체
        token.setRefreshToken(newRefreshToken);
        token.setExpiredAt(LocalDateTime.now().plusWeeks(2));
        tokenRepository.save(token);

        return new TokenResponseDto(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void saveRefreshToken(Long userId, String refreshToken) {
        Token token = tokenRepository.findByUser_Id(userId)
                .map(existing -> {
                    existing.setRefreshToken(refreshToken);
                    existing.setExpiredAt(LocalDateTime.now().plusWeeks(2));
                    return existing;
                })
                .orElseGet(() -> Token.builder()
                        .user(User.builder().id(userId).build())
                        .refreshToken(refreshToken)
                        .expiredAt(LocalDateTime.now().plusWeeks(2))
                        .build()
                );

        tokenRepository.save(token);
    }
}
