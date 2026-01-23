package com.panda.authappbackend.services;

import com.panda.authappbackend.dtos.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserByEmail(String email);
    UserDto updateUser(UserDto userDto, String userId);
    void deleteUser(String email);
    UserDto getUserById(String userId);
    Iterable<UserDto> getAllUsers();
}
