package com.myfitmate.myfitmate.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SignupRequestDto {

    @JsonProperty("username")
    private String username;

    @JsonProperty("realName")
    private String realName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("birthDate")
    private String birthDate;

    @JsonProperty("heightCm")
    private Float heightCm;

    @JsonProperty("weightKg")
    private Float weightKg;

    @JsonProperty("goal")
    private String goal;
}
