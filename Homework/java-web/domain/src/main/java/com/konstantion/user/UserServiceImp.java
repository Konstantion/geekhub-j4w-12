package com.konstantion.user;

import com.konstantion.exceptions.BadRequestException;
import com.konstantion.exceptions.ForbiddenException;
import com.konstantion.exceptions.RegistrationException;
import com.konstantion.ragistration.token.ConfirmationToken;
import com.konstantion.ragistration.token.ConfirmationTokenService;
import com.konstantion.user.dto.UpdateUserDto;
import com.konstantion.user.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.konstantion.user.Role.ADMIN;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public record UserServiceImp(
        UserRepository userRepository,
        BCryptPasswordEncoder passwordEncoder,
        ConfirmationTokenService tokenService
) implements UserService {
    private static final UserMapper userMapper = UserMapper.INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);
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
    public UUID enableUser(User user) {
        return userRepository.setEnableById(user.getId(), true);
    }

    @Override
    public UUID editUser(UUID uuid, UpdateUserDto updateDto, User user) {
        return null;
    }

    @Override
    public UUID deleteUser(UUID uuid, User user) {
        return null;
    }

    @Override
    public UserDto getUser(UUID uuid, User requester) {
        if (!requester.getId().equals(uuid)
            && !requester.hasRole(ADMIN)) {
            throw new ForbiddenException("You don't have enough permissions to do this operation");
        }

        User user = findByIdOrThrow(uuid);

        return userMapper.toDto(user);
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

    private User findByIdOrThrow(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> {
           throw new BadRequestException(format("User with id %s doesn't exist",id));
        });
    }
}
