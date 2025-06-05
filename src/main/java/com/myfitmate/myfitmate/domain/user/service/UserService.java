package com.myfitmate.myfitmate.domain.user.service;

import com.myfitmate.myfitmate.domain.user.Gender;
import com.myfitmate.myfitmate.domain.user.Goal;
import com.myfitmate.myfitmate.domain.user.dto.LoginRequestDto;
import com.myfitmate.myfitmate.domain.user.entity.User;
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
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("이미 가입된 아이디입니다.");
        }

        Gender gender;
        try {
            gender = Gender.valueOf(dto.getGender().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 성별입니다.");
        }

        Goal goal = Goal.MAINTAIN;
        if (dto.getGoal() != null) {
            try {
                goal = Goal.valueOf(dto.getGoal().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 목표입니다.");
            }
        }
        // birthDate 파싱
        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(dto.getBirthDate());
        } catch (Exception e) {
            throw new IllegalArgumentException("생년월일 형식이 잘못되었습니다. (예: 1990-01-01)");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .realName(dto.getRealName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .gender(gender)
                .birthDate(birthDate)
                .heightCm(dto.getHeightCm())
                .weightKg(dto.getWeightKg())
                .goal(goal)
                .build();


        return userRepository.save(user).getId();
    }

    public String login(LoginRequestDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디가 없습니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user.getUsername();
    }
}
