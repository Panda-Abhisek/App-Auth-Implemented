package com.panda.authappbackend.dtos;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn,
        UserDto user
) {
    public static TokenResponse of(
            String accessToken,
            String refreshToken,
            long expiresIn,
            UserDto user
    ) {
        return new TokenResponse(
                accessToken,
                refreshToken,
                "Bearer",
                expiresIn,
                user
        );
    }
}
