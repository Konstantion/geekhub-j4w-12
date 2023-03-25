package com.konstantion.user;

import com.konstantion.user.dto.UpdateUserDto;
import com.konstantion.user.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface UserService extends UserDetailsService {
    String signUpUser(User user);

    UUID enableUser(User user);

    UUID editUser(UUID uuid, UpdateUserDto updateDto, User requester);

    UUID deleteUser(UUID uuid, User requester);

    UserDto getUser(UUID uuid, User requester);
}
