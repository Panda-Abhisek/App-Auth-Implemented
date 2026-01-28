package com.panda.authappbackend.configs;

public class AppConstants {
    public static final String[] AUTH_PUBLIC_URLS = {
            "/api/v1/auth/**",
            "/actuator/health",
            "/actuator/info",
            "/api/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/webjars/**"
    };

    public static final String[] AUTH_ADMIN_URLS = {
            "/api/v1/admin/**"
    };

    public static final String[] AUTH_USER_URLS = {
            "/api/v1/users/**"
    };

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";
}
