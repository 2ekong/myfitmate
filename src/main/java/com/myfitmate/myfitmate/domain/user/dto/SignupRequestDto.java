package com.myfitmate.myfitmate.domain.user.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequestDto {

    @NotBlank(message = "아이디는 필수입니다.")
    private String username;

    @NotBlank(message = "실명은 필수입니다.")
    private String realName;

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @NotNull(message = "성별은 필수입니다.")
    private String gender;  // ENUM 검증은 컨트롤러에서 수동 검증해도 되고 커스텀 Validator 써도 됨

    @NotNull(message = "생년월일은 필수입니다.")
    private String birthDate;  // yyyy-MM-dd 형식

    private Float heightCm;  // nullable
    private Float weightKg;  // nullable

    private String goal; // optional (기본값: maintain)
}
