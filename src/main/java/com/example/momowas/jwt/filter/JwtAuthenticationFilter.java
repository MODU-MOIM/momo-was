package com.example.momowas.jwt.filter;

import com.example.momowas.error.BusinessException;
import com.example.momowas.error.ExceptionCode;
import com.example.momowas.jwt.util.JwtUtil;
import com.example.momowas.user.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        Long userId = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            userId = jwtUtil.getUserIdFromToken(jwt);

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                ExceptionCode exceptionCode = jwtUtil.validateToken(jwt);

                if (exceptionCode == null) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userId, null, new ArrayList<>());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    setUnauthorizedResponse(response, exceptionCode);
                    return;
                }
            }
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    private void setUnauthorizedResponse(HttpServletResponse response, ExceptionCode exceptionCode) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(String.format(
                "{\"status\": %d, \"code\": \"%s\", \"message\": \"%s\"}",
                exceptionCode.getStatus(), exceptionCode.getCode(), exceptionCode.getMessage()
        ));
        response.getWriter().flush();
    }

}