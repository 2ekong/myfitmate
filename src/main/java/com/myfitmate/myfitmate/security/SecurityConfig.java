package com.myfitmate.myfitmate.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 인증 없이 허용할 경로
                        .requestMatchers("/", "/api/auth/login", "/api/auth/signup", "/api/auth/**", "/api/dev/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll()

                        // GET 요청만 모든 사용자에게 허용
                        .requestMatchers(HttpMethod.GET, "/api/foods/**").permitAll()

                        // 나머지 /api/foods/** 는 인증 필요
                        .requestMatchers(HttpMethod.POST, "/api/foods/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/foods/**").authenticated()

                        // meals 는 모두 인증 필요
                        .requestMatchers("/api/meals/**").authenticated()

                        // 이미 정의된 logout 등은 유지
                        .requestMatchers(HttpMethod.POST, "/api/auth/logout").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/user/me").authenticated()

                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
