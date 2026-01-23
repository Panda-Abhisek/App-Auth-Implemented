package com.panda.authappbackend.services.impl;

import com.panda.authappbackend.dtos.UserDto;
import com.panda.authappbackend.services.UserService;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserDto createUser(UserDto userDto) {
        return null;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        return null;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        return null;
    }

    @Override
    public void deleteUser(String email) {

    }

    @Override
    public UserDto getUserById(String userId) {
        return null;
    }

    @Override
    public Iterator<UserDto> getAllUsers() {
        return null;
    }
}
