package com.panda.authappbackend.services;

import com.panda.authappbackend.dtos.UserDto;

import java.util.Iterator;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserByEmail(String email);
    UserDto updateUser(UserDto userDto, String userId);
    void deleteUser(String email);
    UserDto getUserById(String userId);
    Iterator<UserDto> getAllUsers();
}
