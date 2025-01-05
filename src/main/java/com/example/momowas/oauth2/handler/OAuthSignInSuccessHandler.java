package com.example.momowas.oauth2.handler;

import com.example.momowas.jwt.util.JwtUtil;
import com.example.momowas.oauth2.helper.GoogleUserInfo;
import com.example.momowas.oauth2.helper.KakaoUserInfo;
import com.example.momowas.oauth2.helper.NaverUserInfo;
import com.example.momowas.oauth2.helper.OAuth2UserInfo;
import com.example.momowas.jwt.service.RefreshTokenService;
import com.example.momowas.user.domain.User;
import com.example.momowas.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthSignInSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${jwt.refreshExpiration}")
    private long REFRESH_TOKEN_EXPIRATION_TIME;

    private OAuth2UserInfo oAuth2UserInfo = null;

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication; // 토큰
        final String provider = token.getAuthorizedClientRegistrationId(); // provider 추출

        switch (provider) {
            case "google" -> {
                log.info("구글 로그인 요청");
                oAuth2UserInfo = new GoogleUserInfo(token.getPrincipal().getAttributes());
            }
            case "kakao" -> {
                log.info("카카오 로그인 요청");
                oAuth2UserInfo = new KakaoUserInfo(token.getPrincipal().getAttributes());
            }
            case "naver" -> {
                log.info("네이버 로그인 요청");
                oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) token.getPrincipal().getAttributes().get("response"));
            }
        }

        // 정보 추출
        String providerId = oAuth2UserInfo.getProviderId();
        String name = oAuth2UserInfo.getName();
        String email = oAuth2UserInfo.getName();

        User existUser = userService.findUserByProviderId(providerId);
        User user;

        if (existUser == null) {
            log.info("신규 유저입니다. 등록을 진행합니다.");

            user = User.builder()
                    .email(email)
                    .nickname(name)
                    .score(0)
                    .createdAt(LocalDateTime.now())
                    .gender(null)
                    .age(0)
                    .profileImage(null)
                    .provider(provider)
                    .providerId(providerId)
                    .build();

            userService.create(user);

        } else {
            log.info("기존 유저입니다.");
            user = existUser;
        }

        log.info("유저 이름 : {}", name);
        log.info("PROVIDER : {}", provider);
        log.info("PROVIDER_ID : {}", providerId);

        // 토큰 발급
        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        //redis refresh Token 저장
        refreshTokenService.saveTokenInfo(String.valueOf(user.getId()), refreshToken, accessToken);

        response.setHeader("Authorization", "Bearer " + accessToken);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) REFRESH_TOKEN_EXPIRATION_TIME);

    }

}
