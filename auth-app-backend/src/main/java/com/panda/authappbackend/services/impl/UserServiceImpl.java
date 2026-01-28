package com.panda.authappbackend.services.impl;

import com.panda.authappbackend.configs.AppConstants;
import com.panda.authappbackend.dtos.UserDto;
import com.panda.authappbackend.exceptions.ResourceNotFoundException;
import com.panda.authappbackend.helpers.UserHelper;
import com.panda.authappbackend.models.Provider;
import com.panda.authappbackend.models.Role;
import com.panda.authappbackend.models.User;
import com.panda.authappbackend.repositroies.RoleRepository;
import com.panda.authappbackend.repositroies.UserRepository;
import com.panda.authappbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        if(userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("User with email " + userDto.getEmail() + " already exists");
        }
        if(userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        User user = modelMapper.map(userDto, User.class);
        user.setProvider(userDto.getProvider() != null ? userDto.getProvider() : Provider.LOCAL);
//        log.info("User created to save: {}", user);
        // role assignment logic can be added here
        // TODO: Assign default role to user
        Role defaultRole = roleRepository
                .findByName(AppConstants.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add(defaultRole);

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        UUID uId = UserHelper.parseUserId(userId);
        User existingUser = userRepository.findById(uId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));

        existingUser.setUsername(userDto.getUsername() != null ? userDto.getUsername() : existingUser.getUsername());
        existingUser.setImage(userDto.getImage() != null ? userDto.getImage() : existingUser.getImage());
        existingUser.setEnabled(userDto.getEnabled());
        existingUser.setProvider(userDto.getProvider() != null ? userDto.getProvider() : existingUser.getProvider());
        existingUser.setUpdatedAt(Instant.now());

        // TODO: change password update logic to hash the password
        existingUser.setPassword(userDto.getPassword() != null ? userDto.getPassword() : existingUser.getPassword());

        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public void deleteUser(String userId) {
        UUID uId = UserHelper.parseUserId(userId);
        User user = userRepository.findById(uId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        userRepository.delete(user);
    }

    @Override
    public UserDto getUserById(String userId) {
        UUID uId = UserHelper.parseUserId(userId);
        User user = userRepository.findById(uId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public Iterable<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }
}
