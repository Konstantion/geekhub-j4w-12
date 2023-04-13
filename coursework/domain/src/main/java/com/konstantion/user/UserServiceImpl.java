package com.konstantion.user;

import com.konstantion.exception.BadRequestException;
import com.konstantion.exception.ForbiddenException;
import com.konstantion.user.model.CreateUserRequest;
import com.konstantion.user.validator.UserValidator;
import com.konstantion.utils.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.konstantion.exception.utils.ExceptionMessages.NOT_ENOUGH_AUTHORITIES;
import static com.konstantion.exception.utils.ExceptionUtils.nonExistingIdSupplier;
import static com.konstantion.user.Permission.CREATE_WAITER;
import static com.konstantion.user.Permission.getDefaultWaiterPermission;
import static com.konstantion.user.Role.ADMIN;
import static com.konstantion.user.Role.getWaiterRole;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;

@Component
public record UserServiceImpl(
        UserPort userPort,
        UserValidator userValidator,
        PasswordEncoder passwordEncoder
) implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User getUserById(UUID uuid) {

        return getUserByIdOrThrow(uuid);
    }

    @Override
    public User createWaiter(CreateUserRequest request, User user) {
        if (user.hasNoPermission(ADMIN, CREATE_WAITER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        ValidationResult validationResult = userValidator.validate(request);
        validationResult.validOrTrow();

        List<User> dbUsers = userPort.findAll();

        if (matchAnyUserWithEmail(dbUsers, request.email())) {
            throw new BadRequestException(format("User with email %s already exist", request.email()));
        }

        User waiter = buildUserFromCreateRequest(request);

        userPort.save(waiter);

        logger.info("User {} successfully created", user);

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userPort.findByEmail(username).orElseThrow(() -> {
                    throw new UsernameNotFoundException(format("User with username %s doesn't exist", username));
                }
        );
    }

    private User getUserByIdOrThrow(UUID uuid) {
        return userPort.findById(uuid)
                .orElseThrow(nonExistingIdSupplier(User.class, uuid));
    }

    private User buildUserFromCreateRequest(CreateUserRequest createUserRequest) {
        return User.builder()
                .email(createUserRequest.email())
                .firstName(createUserRequest.firstName())
                .lastName(createUserRequest.lastName())
                .phoneNumber(createUserRequest.phoneNumber())
                .age(createUserRequest.age())
                .roles(getWaiterRole())
                .permissions(getDefaultWaiterPermission())
                .password(passwordEncoder.encode(createUserRequest.password()))
                .active(true)
                .createdAt(now())
                .build();
    }

    private boolean matchAnyUserWithEmail(List<User> users, String email) {
        return users.stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }
}
