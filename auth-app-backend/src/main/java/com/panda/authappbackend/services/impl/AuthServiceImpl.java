package com.panda.authappbackend.services.impl;

import com.panda.authappbackend.dtos.UserDto;
import com.panda.authappbackend.services.AuthService;
import com.panda.authappbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto registerUser(UserDto userDto) {

        // password encoding
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // you can add additional registration logic here:
        // - e.g., sending confirmation email
        // - e.g., validating password strength
        // - e.g., assigning default roles
        // For now, we simply delegate to userService to create the user
        return userService.createUser(userDto);
    }

}
