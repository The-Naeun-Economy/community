package com.economy.community.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component  // 이 어노테이션을 추가하여 Spring이 빈으로 등록하도록 합니다
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        System.out.println("JwtAuthenticationFilter: Request URL - " + request.getRequestURI());

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " 제거
            System.out.println("Extracted Token: " + token);

            if (jwtUtil.isTokenValid(token)) {
                Long userId = jwtUtil.getUserIdFromToken(token);
                String userNickname = jwtUtil.getUserNicknameFromToken(token);

                System.out.println("Authenticated UserId: " + userId + ", UserNickname: " + userNickname);

                UserAuthentication authentication = new UserAuthentication(userId, userNickname);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                System.out.println("Invalid Token");
            }
        } else {
            System.out.println("Authorization header missing or does not start with Bearer");
        }

        chain.doFilter(request, response);
    }
}