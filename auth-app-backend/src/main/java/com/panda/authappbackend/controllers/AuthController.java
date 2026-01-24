package com.panda.authappbackend.controllers;

import com.panda.authappbackend.dtos.LoginRequest;
import com.panda.authappbackend.dtos.TokenResponse;
import com.panda.authappbackend.dtos.UserDto;
import com.panda.authappbackend.models.User;
import com.panda.authappbackend.repositroies.UserRepository;
import com.panda.authappbackend.security.JwtService;
import com.panda.authappbackend.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authenticate = authenticate(loginRequest);
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new BadCredentialsException("User not found"));
        if(!user.isEnabled()) {
            throw new BadCredentialsException("User account is disabled");
        }
        String accessToken = jwtService.generateAccessToken(user);
        TokenResponse tokenResponse = TokenResponse.of(accessToken, "", jwtService.getAccessTtlSeconds(), modelMapper.map(user, UserDto.class));
        return ResponseEntity.ok(tokenResponse);
    }

    private Authentication authenticate(LoginRequest loginRequest) {
        try {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {
        UserDto registeredUser = authService.registerUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

}
