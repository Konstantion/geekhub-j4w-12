package com.konstantion.configuration;

import com.konstantion.user.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AuthenticationConfig {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthenticationConfig(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
}
