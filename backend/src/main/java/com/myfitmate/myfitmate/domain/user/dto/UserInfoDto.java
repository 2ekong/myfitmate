package com.myfitmate.myfitmate.domain.user.dto;

import com.myfitmate.myfitmate.domain.user.Gender;
import com.myfitmate.myfitmate.domain.user.Goal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoDto {
    private String username;
    private String realName;
    private String email;
    private String nickname;
    private Gender gender;
    private String birthDate;
    private Float heightCm;
    private Float weightKg;
    private Goal goal;
}
