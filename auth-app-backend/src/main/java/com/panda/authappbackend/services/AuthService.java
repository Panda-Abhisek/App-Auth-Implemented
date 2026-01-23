package com.panda.authappbackend.services;

import com.panda.authappbackend.dtos.UserDto;

public interface AuthService {
    UserDto registerUser(UserDto userDto);
}
