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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        // 인증 없이 허용할 경로
                        .requestMatchers("/", "/login", "/signup").permitAll()
                        .requestMatchers("/api/auth/**", "/api/ai/**", "/api/dev/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll()

                        // Foods - GET만 허용, 나머지는 인증 필요
                        .requestMatchers(HttpMethod.GET, "/api/foods/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/foods/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/foods/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/foods/**").authenticated()

                        // Meals - 전체 인증 필요
                        .requestMatchers("/api/meals/**").authenticated()

                        // Statistics - 인증 필요
                        .requestMatchers("/api/statistics/**").authenticated()

                        // Exercise - 쓰기 관련만 인증
                        .requestMatchers(HttpMethod.POST, "/api/exercise/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/exercise/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/exercise/**").authenticated()

                        // 사용자 관련
                        .requestMatchers(HttpMethod.DELETE, "/api/user/me").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/auth/logout").authenticated()

                        // 기타 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:5173")); // 프론트엔드 주소
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
