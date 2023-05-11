package com.konstantion.utils;

import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtils {
    private PasswordUtils() {
    }

    public static boolean nullSaveMatches(String raw, String encoded, PasswordEncoder passwordEncoder) {
        if (raw == null || encoded == null) {
            return false;
        }

        return passwordEncoder.matches(raw, encoded);
    }
}
