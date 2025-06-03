package com.myfitmate.myfitmate.domain.user.service;

import com.myfitmate.myfitmate.domain.user.Gender;
import com.myfitmate.myfitmate.domain.user.Goal;
import com.myfitmate.myfitmate.domain.user.User;
import com.myfitmate.myfitmate.domain.user.dto.SignupRequestDto;
import com.myfitmate.myfitmate.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public long signup(SignupRequestDto dto) {
        if (userRepository.existsByUsername(dto.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 아이디입니다.");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .realName(dto.getRealName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .gender(Gender.valueOf(dto.getGender().toUpperCase()))
                .birthDate(LocalDate.parse(dto.getBirthDate()))
                .heightCm(dto.getHeightCm())
                .weightKg(dto.getWeightKg())
                .goal(dto.getGoal() != null ? Goal.valueOf(dto.getGoal().toUpperCase()) : Goal.MAINTAIN)
                .build();


        return userRepository.save(user).getId();
    }
}
