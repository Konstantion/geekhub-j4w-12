package com.konstantion.user;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface UserService extends UserDetailsService {
    UUID signUpUser(User user);

    UUID enableUser(String email);
}
