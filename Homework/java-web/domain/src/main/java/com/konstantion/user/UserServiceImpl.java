package com.konstantion.user;

import com.konstantion.email.EmailService;
import com.konstantion.exceptions.BadRequestException;
import com.konstantion.exceptions.ForbiddenException;
import com.konstantion.exceptions.RegistrationException;
import com.konstantion.exceptions.ValidationException;
import com.konstantion.ragistration.token.ConfirmationToken;
import com.konstantion.ragistration.token.ConfirmationTokenService;
import com.konstantion.user.model.CreateUserRequest;
import com.konstantion.user.model.UpdateUserRequest;
import com.konstantion.user.model.UpdateUserRolesRequest;
import com.konstantion.user.validator.UserValidator;
import com.konstantion.utils.PasswordUtils;
import com.konstantion.utils.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.konstantion.user.Role.ADMIN;
import static com.konstantion.user.Role.MODERATOR;
import static com.konstantion.utils.validator.ValidationResult.validOrThrow;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public record UserServiceImpl(
        UserRepository userRepository,
        BCryptPasswordEncoder passwordEncoder,
        ConfirmationTokenService tokenService,
        UserValidator userValidator,
        EmailService emailService
) implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String USER_NOT_FOUND_MSG =
            "user with email %s not found";


    @Override
    public String signUpUser(User user) {
        Optional<User> userOptional = userRepository
                .findByEmail(user.getEmail());

        boolean userExists = userOptional.isPresent();

        if (userExists) {
            User existingUser = userOptional.get();

            List<ConfirmationToken> confirmationTokens = tokenService
                    .getTokens(existingUser)
                    .orElseThrow(() -> {
                        throw new RegistrationException("Email already taken by another user");
                    });

            boolean hasConfirmedToken = confirmationTokens.stream()
                    .anyMatch(token -> nonNull(token.confirmedAt()));

            if (hasConfirmedToken) {
                throw new RegistrationException("User with this email already registered");
            }

            boolean hasTokenToConfirm = confirmationTokens.stream()
                    .filter(token -> isNull(token.confirmedAt()))
                    .anyMatch(token -> token.expiresAt().isAfter(LocalDateTime.now()));

            if (hasTokenToConfirm) {
                throw new RegistrationException("User with this email already have token to confirm");
            } else {
                String token = UUID.randomUUID().toString();

                tokenService.createAndSaveConfirmationToken(token, existingUser);

                return token;
            }
        }

        String encodedPassword = passwordEncoder
                .encode(user.getPassword());

        user.setPassword(encodedPassword);

        userRepository.save(user);

        String token = UUID.randomUUID().toString();

        tokenService.createAndSaveConfirmationToken(token, user);

        return token;
    }

    @Override
    public UUID enableUser(UUID uuid) {
        User user = findByIdOrThrow(uuid);
        return userRepository.setEnableById(uuid, true);
    }

    @Override
    public User editUser(UUID uuid, UpdateUserRequest updateUserRequest, User authorized) {
        if (!authorized.getId().equals(uuid)
            && authorized.hasRole(ADMIN)) {
            throw new ForbiddenException("Not enough authorities");
        }

        User user = findByIdOrThrow(uuid);

        ValidationResult validationResult = userValidator.validate(updateUserRequest);
        if (validationResult.errorsPresent()) {
            throw new ValidationException(
                    format("Failed to update user with id %s", uuid),
                    validationResult.getErrorsAsMap()
            );
        }

        updateUser(user, updateUserRequest);

        userRepository.save(user);

        return null;
    }

    @Override
    public UUID disableUser(UUID uuid) {
        User user = findByIdOrThrow(uuid);
        userRepository.setEnableById(uuid, false);

        return uuid;
    }

    @Override
    public User editUserRoles(UUID uuid, UpdateUserRolesRequest updateUserRolesRequest) {
        User user = findByIdOrThrow(uuid);

        updateUserRoles(user, updateUserRolesRequest);
        userRepository.save(user);

        return user;
    }

    @Override
    public String restorePassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new BadRequestException(format("User with email %s doesn't exist", email));
        });

        String tempPassword = PasswordUtils.generatePassword();

        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);

        emailService.send(
                email,
                emailService.buildRestorePasswordEmail(tempPassword)
        );

        return tempPassword;
    }

    @Override
    public User getUser(UUID uuid, User authorized) {
        if (!authorized.getId().equals(uuid)
            && !authorized.hasRole(ADMIN)) {
            throw new ForbiddenException("You don't have enough permissions to do this operation");
        }

        return findByIdOrThrow(uuid);
    }

    @Override
    public User createAdmin(CreateUserRequest request) {
        return createUserWithRoles(request, Set.of(ADMIN));
    }

    @Override
    public User createModerator(CreateUserRequest request) {
        return createUserWithRoles(request, Set.of(MODERATOR));
    }

    @Override
    public User delete(UUID id) {
        User user = findByIdOrThrow(id);

        userRepository.delete(user);

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                format(USER_NOT_FOUND_MSG, username)
                        )
                );
    }

    private void updateUser(User user, UpdateUserRequest updateUserRequest) {
        user.setFirstName(updateUserRequest.firstName());
        user.setLastName(updateUserRequest.lastName());
        user.setPhoneNumber(updateUserRequest.phoneNumber());
        user.setPassword(passwordEncoder.encode(updateUserRequest.password()));
    }

    private void updateUserRoles(User user, UpdateUserRolesRequest updateUserRolesRequest) {
        Set<Role> updatedUserRole = new HashSet<>();
        Set<String> roleNames = Arrays.stream(Role.values())
                .map(Role::name).collect(Collectors.toSet());
        updateUserRolesRequest.roles().forEach(roleName -> {
            String upperCaseRoleName = roleName.toUpperCase();
            if (roleNames.contains(upperCaseRoleName)) {
                updatedUserRole.add(Role.valueOf(upperCaseRoleName));
            }
        });

        user.setRoles(updatedUserRole);
    }

    private User findByIdOrThrow(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> {
            throw new BadRequestException(format("User with id %s doesn't exist", id));
        });
    }

    private User createUserWithRoles(CreateUserRequest request, Set<Role> roles) {
        ValidationResult validationResult = userValidator.validate(request);
        validOrThrow(validationResult);

        userRepository.findByEmail(request.email()).ifPresent(dnUser -> {
            throw new RegistrationException("Email already taken by another user");
        });


        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .roles(roles)
                .enabled(true)
                .accountNonLocked(true)
                .password(passwordEncoder.encode(request.password()))
                .build();

        userRepository.save(user);

        return user;
    }
}
