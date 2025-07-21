package com.myfitmate.myfitmate.security;

import com.myfitmate.myfitmate.domain.user.entity.User;
import com.myfitmate.myfitmate.domain.user.service.CustomerUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomerUserDetailService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // 인증 없이 접근 가능한 경로
        if (
                path.startsWith("/api/auth/") ||
                        path.startsWith("/api/ai/") ||
                        path.equals("/") ||
                        path.equals("/login") ||
                        path.equals("/signup")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String header = request.getHeader("Authorization");

            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                Long userId = jwtUtil.extractUserId(token); // 유효성 검증 포함되어야 안정적

                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetailsImpl userDetails = userDetailsService.loadUserById(userId);
                    JwtAuthenticationToken auth = new JwtAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception e) {
            logger.warn("JWT 필터 처리 실패: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
