package com.myfitmate.myfitmate.domain.user.controller;

import com.myfitmate.myfitmate.domain.user.dto.TokenResponseDto;
import com.myfitmate.myfitmate.domain.user.service.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;

    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code) {

        log.info("kakaoLogin 진입 - code: {}", code);
        TokenResponseDto token = kakaoService.kakaoLogin(code);
        return ResponseEntity.ok(token);
    }
}
