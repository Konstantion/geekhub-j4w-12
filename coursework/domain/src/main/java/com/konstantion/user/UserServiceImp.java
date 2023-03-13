package com.konstantion.user;

import com.konstantion.user.dto.CreationUserDto;
import com.konstantion.user.dto.UserDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public record UserServiceImp(
        UserRepository userRepository
) implements UserService {

    @Override
    public UserDto getUserById(UUID uuid) {
        return null;
    }

    @Override
    public User saveTableUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserDto createUser(CreationUserDto user) {
        return null;
    }
}
