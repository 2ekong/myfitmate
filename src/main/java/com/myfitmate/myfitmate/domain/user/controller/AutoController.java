package com.myfitmate.myfitmate.domain.user.controller;

import com.myfitmate.myfitmate.domain.user.dto.LoginRequestDto;
import com.myfitmate.myfitmate.domain.user.dto.SignupRequestDto;
import com.myfitmate.myfitmate.domain.user.entity.User;
import com.myfitmate.myfitmate.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AutoController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequestDto dto) {
        long userId = userService.signup(dto);
        return ResponseEntity.ok("회원가입 성공. ID: " + userId);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto dto) {
        String username = userService.login(dto);
        return ResponseEntity.ok("로그인 성공" + username);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyPage(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok("내 정보: " + user.getNickname());
    }
}
