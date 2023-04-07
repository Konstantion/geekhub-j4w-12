package com.konstantion.utils;

import java.security.SecureRandom;

public class PasswordUtils {
    private static final String SYMBOLS = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
    private static final String DIGITS = "0123456789";
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String generatePassword() {
        StringBuilder passwordBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();


        int randomDigitIndex = random.nextInt(DIGITS.length());
        passwordBuilder.append(DIGITS.charAt(randomDigitIndex));


        int randomSymbolIndex = random.nextInt(SYMBOLS.length());
        passwordBuilder.append(SYMBOLS.charAt(randomSymbolIndex));


        String combinedChars = LETTERS + DIGITS + SYMBOLS;
        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(combinedChars.length());
            passwordBuilder.append(combinedChars.charAt(randomIndex));
        }

        return passwordBuilder.toString();
    }

    private PasswordUtils() {

    }
}
