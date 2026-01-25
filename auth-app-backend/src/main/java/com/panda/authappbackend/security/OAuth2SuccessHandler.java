package com.panda.authappbackend.security;

import com.panda.authappbackend.models.Provider;
import com.panda.authappbackend.models.RefreshToken;
import com.panda.authappbackend.models.User;
import com.panda.authappbackend.repositroies.RefreshTokenRepository;
import com.panda.authappbackend.repositroies.UserRepository;
import com.panda.authappbackend.services.impl.CookieService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final Logger log = LoggerFactory.getLogger(OAuth2SuccessHandler.class);
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieService cookieService;

    @Value("${app.auth.frontend.success-redirect-url}")
    private String frontendSuccessUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        // You can extract user details from oAuth2User and perform necessary actions here

        String registrationId = "UNKNOWN";
        if(authentication instanceof OAuth2AuthenticationToken token) {
            registrationId = token.getAuthorizedClientRegistrationId();
        }

        User user;
        switch (registrationId) {
            case "google" -> {
                String googleId = oAuth2User.getAttributes().getOrDefault("sub", "").toString();
                String email = oAuth2User.getAttributes().getOrDefault("email", "").toString();
                String name = oAuth2User.getAttributes().getOrDefault("name", "").toString();
                String picture = oAuth2User.getAttributes().getOrDefault("picture", "").toString();
                User newUser = User.builder()
                        .provider(Provider.GOOGLE)
                        .email(email)
                        .username(name)
                        .enabled(true)
                        .providerId(googleId)
                        .image(picture)
                        .build();

                user = userRepository.findByEmail(email).orElseGet(() -> userRepository.save(newUser));
            }
            case "github" -> {
                String githubId = oAuth2User.getAttributes().getOrDefault("id", "").toString();
                String name = oAuth2User.getAttributes().getOrDefault("login", "").toString();
                String picture = oAuth2User.getAttributes().getOrDefault("avatar_url", "").toString();
                String email = (String) oAuth2User.getAttributes().get("email");
                if(email == null || email.isBlank()) {
                    // GitHub may not provide email if it's private, handle accordingly
                    email = name + "@github.com"; // Fallback email
                }

                User newUser = User.builder()
                        .provider(Provider.GITHUB)
                        .email(email)
                        .username(name)
                        .providerId(githubId)
                        .enabled(true)
                        .image(picture)
                        .build();

                user = userRepository.findByEmail(email).orElseGet(() -> userRepository.save(newUser));
            }
            default -> throw new IllegalArgumentException("Unsupported registration ID: " + registrationId);
        }

        String jti = UUID.randomUUID().toString();
        RefreshToken refreshTokenOb = RefreshToken.builder()
                .jti(jti)
                .user(user)
                .revoked(false)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
                .build();
        refreshTokenRepository.save(refreshTokenOb);
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user, refreshTokenOb.getJti());
        cookieService.attachRefreshCookie(response, refreshToken, (int) jwtService.getRefreshTtlSeconds());
//        response.getWriter().write("Login Successful. Access Token: " + accessToken);
        response.sendRedirect(frontendSuccessUrl);
    }
}
