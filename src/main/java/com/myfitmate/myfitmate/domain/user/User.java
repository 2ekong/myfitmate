package com.myfitmate.myfitmate.domain.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String realName;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(name = "kakao_id", unique = true)
    private String kakaoId;

    private String nickname;

    @Enumerated(EnumType.STRING)    //DB에 0, 1말고 문자열로 저장
    private Gender gender;

    private LocalDate birthDate;

    private Float heightCm;

    private Float weightKg;

    @Builder.Default
    private Goal goal = Goal.MAINTAIN;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
