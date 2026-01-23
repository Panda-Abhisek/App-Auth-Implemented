package com.panda.authappbackend.helpers;

import java.util.UUID;

public class UserHelper {
    public static UUID parseUserId(String userId) {
        try {
            return UUID.fromString(userId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid user ID format");
        }
    }
}
