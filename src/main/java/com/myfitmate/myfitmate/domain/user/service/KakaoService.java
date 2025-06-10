package com.myfitmate.myfitmate.domain.user.service;

import com.myfitmate.myfitmate.domain.user.Gender;
import com.myfitmate.myfitmate.domain.user.Goal;
import com.myfitmate.myfitmate.security.JwtUtil;
import com.myfitmate.myfitmate.domain.user.entity.User;
import com.myfitmate.myfitmate.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public String kakaoLogin(String code) {
        // 1. 카카오 액세스 토큰 요청
        String tokenUri = "https://kauth.kakao.com/oauth/token";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenUri, params, Map.class);
        String accessToken = (String) tokenResponse.getBody().get("access_token");

        // 2. 사용자 정보 요청
        String userInfoUri = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, Map.class);
        Map<String, Object> userInfo = userInfoResponse.getBody();

        // 3. 사용자 정보 추출
        Long kakaoId = ((Number) userInfo.get("id")).longValue();
        String email = "no-email@kakao.com";
        String nickname = "unknown";

        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        if (kakaoAccount != null) {
            Object emailObj = kakaoAccount.get("email");
            if (emailObj != null) email = emailObj.toString();

            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            if (profile != null) {
                Object nicknameObj = profile.get("nickname");
                if (nicknameObj != null) nickname = nicknameObj.toString();
            }
        }

        // 4. 유저 조회 또는 생성 (람다 제거)
        User user = userRepository.findByKakaoId(kakaoId).orElse(null);

        if (user == null) {
            user = new User();
            user.setKakaoId(kakaoId);
            user.setEmail(email);
            user.setNickname(nickname);
            user.setPassword(""); // 카카오는 패스워드 없음
            user.setUsername("kakao_" + kakaoId);
            user.setRealName(nickname);
            user.setBirthDate(null);
            user.setGender(null);
            user.setGoal(null);
            user.setHeightCm(null); // Of로 하면 float형 0.0이란 소리이다. 그럼 통계할때 말도 안 됨
            user.setWeightKg(null);

            user = userRepository.save(user);
        }

        // 5. JWT 토큰 생성
        return jwtUtil.createToken(user.getId(), user.getNickname());
    }
}
