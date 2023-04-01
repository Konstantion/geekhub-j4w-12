package com.konstantion.user;

import com.konstantion.user.dto.CreationUserDto;
import com.konstantion.user.dto.UserDto;

import java.util.UUID;

public interface UserService {
    UserDto getUserById(UUID uuid);

    UserDto createWaiter(CreationUserDto cudto, User user);
}
