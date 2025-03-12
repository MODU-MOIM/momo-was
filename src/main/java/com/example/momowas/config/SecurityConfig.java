package com.example.momowas.config;

import com.example.momowas.jwt.filter.JwtAuthenticationFilter;
import com.example.momowas.oauth2.handler.OAuthSignInFailureHandler;
import com.example.momowas.oauth2.handler.OAuthSignInSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuthSignInSuccessHandler oAuthSignInSuccessHandler;
    private final OAuthSignInFailureHandler oAuthSignInFailureHandler;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String[] publicEndpoints = {
            "/ws/**",
            "/stomp/**",
            "/auth/find-id",
            "/auth/reset-pw",
            "/auth/sign-in",
            "/auth/sign-up",
            "/auth/reissue",
            "/auth/send-sms",
            "/auth/verify-code",
            "/crews/popular",
            "/feeds/popular",
            "/regions/hot",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/error",
            "/chat/**",
            "/chat-rooms/{roomId}",
            "/index.html",
    };
    //비밀번호 암호화
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000","http://13.124.54.225:8080", "https://modumoim.site","https://modumoim.site:8080","http://localhost:9200","https://modumoim.site:9200","http://172.22.41.212","http://172.22.41.212:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true); // 인증 정보를 포함한 요청 허용
        configuration.setExposedHeaders(List.of("Authorization")); //Authorization 헤더 노출
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers(publicEndpoints).permitAll() // 공개 엔드포인트 설정
                                .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
                )
                .oauth2Login(oauth ->
                        oauth
                                .successHandler(oAuthSignInSuccessHandler)
                                .failureHandler(oAuthSignInFailureHandler)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가

        return httpSecurity.build();
    }

}
