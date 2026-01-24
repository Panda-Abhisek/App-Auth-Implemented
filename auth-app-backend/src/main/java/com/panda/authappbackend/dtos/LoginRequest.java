package com.panda.authappbackend.dtos;

public record LoginRequest(
        String email,
        String password
) {
}
