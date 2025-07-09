package com.myfitmate.myfitmate.domain.user.controller;

import com.myfitmate.myfitmate.domain.user.dto.LoginRequestDto;
import com.myfitmate.myfitmate.domain.user.dto.SignupRequestDto;
import com.myfitmate.myfitmate.domain.user.dto.TokenResponseDto;
import com.myfitmate.myfitmate.domain.user.entity.User;
import com.myfitmate.myfitmate.domain.user.service.TokenService;
import com.myfitmate.myfitmate.domain.user.service.UserService;
import com.myfitmate.myfitmate.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AutoController {

    private final UserService userService;
    private final TokenService tokenService;


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequestDto dto) {
        long userId = userService.signup(dto);
        return ResponseEntity.ok("회원가입 성공. ID: " + userId);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto dto) {
        TokenResponseDto tokens = userService.login(dto);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        tokenService.deleteRefreshToken(userDetails.getUser().getId());
        return ResponseEntity.ok("로그아웃 완료");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader("Authorization") String refreshTokenHeader) {
        String refreshToken = refreshTokenHeader.replace("Bearer ", "");
        TokenResponseDto newTokens = tokenService.refreshAllTokens(refreshToken);
        return ResponseEntity.ok(newTokens);
    }
}
