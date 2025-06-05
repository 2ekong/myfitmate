package com.myfitmate.myfitmate.domain.user.controller;

import com.myfitmate.myfitmate.domain.user.dto.LoginRequestDto;
import com.myfitmate.myfitmate.domain.user.dto.SignupRequestDto;
import com.myfitmate.myfitmate.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
