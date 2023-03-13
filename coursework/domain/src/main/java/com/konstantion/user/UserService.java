package com.konstantion.user;

import com.konstantion.user.dto.CreationUserDto;
import com.konstantion.user.dto.UserDto;

import java.util.UUID;

public interface UserService {
    UserDto getUserById(UUID uuid);

    User saveTableUser(User user);

    UserDto createUser(CreationUserDto user);
}
