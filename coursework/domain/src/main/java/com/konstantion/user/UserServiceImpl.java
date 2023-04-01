package com.konstantion.user;

import com.konstantion.exception.BadRequestException;
import com.konstantion.exception.ForbiddenException;
import com.konstantion.user.dto.CreationUserDto;
import com.konstantion.user.dto.UserDto;
import com.konstantion.user.validator.UserValidator;
import com.konstantion.utils.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.konstantion.user.Permission.CREATE_WAITER;
import static com.konstantion.user.Permission.getDefaultWaiterPermission;
import static com.konstantion.user.Role.ADMIN;
import static com.konstantion.user.Role.getWaiterRole;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;

@Component
public record UserServiceImpl(
        UserRepository userRepository,
        UserValidator userValidator,
        PasswordEncoder passwordEncoder
) implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    public UserDto getUserById(UUID uuid) {
        User user = getUserByIdOrThrow(uuid);

        return userMapper.toDto(user);
    }

    @Override
    public UserDto createWaiter(CreationUserDto cudto, User user) {
        if (user.hasNoPermission(ADMIN, CREATE_WAITER)) {
            throw new ForbiddenException("Not enough authorities to create waiter");
        }

        ValidationResult validationResult = userValidator.validate(cudto);
        validationResult.validOrTrow();

        User waiter = User.builder()
                .username(cudto.username())
                .firstName(cudto.firstName())
                .lastName(cudto.lastName())
                .phoneNumber(cudto.phoneNumber())
                .age(cudto.age())
                .roles(getWaiterRole())
                .permissions(getDefaultWaiterPermission())
                .password(passwordEncoder.encode(cudto.password()))
                .active(true)
                .createdAt(now())
                .build();

        userRepository.save(waiter);

        logger.info("User {} successfully created", user);

        return userMapper.toDto(user);
    }

    private User getUserByIdOrThrow(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(() -> {
            throw new BadRequestException(format("User with id %s doesn't exist", uuid));
        });
    }
}
