package com.myfitmate.myfitmate.domain.user.controller;

import com.myfitmate.myfitmate.domain.user.dto.UpdateUserRequestDto;
import com.myfitmate.myfitmate.domain.user.dto.UserInfoDto;
import com.myfitmate.myfitmate.domain.user.entity.User;
import com.myfitmate.myfitmate.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyPage(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(new UserInfoDto(
                user.getUsername(),
                user.getRealName(),
                user.getEmail(),
                user.getNickname(),
                user.getGender(),
                user.getBirthDate().toString(),
                user.getHeightCm(),
                user.getWeightKg(),
                user.getGoal()
        ));
    }

    @PatchMapping("/me")    // PATCH 요청으로 자기 정보 수정하는 것이니까 PatchMapping을 사용한다.
    public ResponseEntity<?> updateMyPage(@AuthenticationPrincipal User user, @RequestBody UpdateUserRequestDto dto) {
        userService.updateUser(user, dto);
        return ResponseEntity.ok("정보 수정 완료");
    }
}
