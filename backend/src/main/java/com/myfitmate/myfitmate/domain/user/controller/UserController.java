package com.myfitmate.myfitmate.domain.user.controller;

import com.myfitmate.myfitmate.domain.user.dto.UpdateUserRequestDto;
import com.myfitmate.myfitmate.domain.user.dto.UserInfoDto;
import com.myfitmate.myfitmate.domain.user.entity.User;
import com.myfitmate.myfitmate.domain.user.service.UserService;
import com.myfitmate.myfitmate.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
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

    @PatchMapping("/me")
    public ResponseEntity<?> updateMyPage(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @RequestBody UpdateUserRequestDto dto) {
        userService.updateUser(userDetails.getUser(), dto);
        return ResponseEntity.ok("정보 수정 완료");
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteMe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deleteUser(userDetails.getUser().getId());
        return ResponseEntity.ok("회원 탈퇴 완료");
    }
}
