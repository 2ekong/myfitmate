package com.myfitmate.myfitmate.domain.user.dto;

import lombok.Getter;

@Getter
public class UpdateUserRequestDto {
    private String nickname;
    private Float heightCm;
    private Float weightKg;
    private String goal;
}
