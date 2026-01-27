package com.panda.authappbackend.security;

import com.panda.authappbackend.helpers.UserHelper;
import com.panda.authappbackend.repositroies.UserRepository;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            // Here you would typically validate the token and set the authentication in the security context
            try {
                // Validate token logic goes here
                if (jwtService.isAccessToken(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    Jws<Claims> jws = jwtService.parse(token);
                    Claims claims = jws.getPayload();
                    UUID userId = UserHelper.parseUserId(claims.getSubject());
                    userRepository.findById(userId).ifPresent(user -> {
                        List<GrantedAuthority> authorities = user.getRoles() == null ? java.util.List.of() :
                                user.getRoles().stream()
                                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName()))
                                .collect(Collectors.toList());
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                user.getEmail(),
                                null,
                                authorities
                        );
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    });
                }
            } catch (ExpiredJwtException e) {
                request.setAttribute("error", "Token has expired");
            } catch (MalformedJwtException e) {
                request.setAttribute("error", "Invalid token");
            } catch (JwtException e) {
                request.setAttribute("error", "Token error");
            } catch (Exception e) {
                request.setAttribute("error", "Authentication error - Invalid token");
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/oauth2/")
                || path.startsWith("/login")
                || path.startsWith("/error")
                || path.endsWith(".css")
                || path.endsWith(".js")
                || path.endsWith(".ico");
    }
}
