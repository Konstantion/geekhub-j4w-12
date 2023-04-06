package com.konstantion.ragistration;

import com.konstantion.email.EmailService;
import com.konstantion.exceptions.BadRequestException;
import com.konstantion.exceptions.RegistrationException;
import com.konstantion.jwt.JwtService;
import com.konstantion.ragistration.token.ConfirmationToken;
import com.konstantion.ragistration.token.ConfirmationTokenService;
import com.konstantion.user.Role;
import com.konstantion.user.User;
import com.konstantion.user.UserRepository;
import com.konstantion.user.UserService;
import com.konstantion.user.model.LoginUserRequest;
import com.konstantion.user.model.RegistrationUserRequest;
import com.konstantion.user.validator.UserValidator;
import com.konstantion.utils.validator.ValidationResult;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Set;

import static java.lang.String.format;
import static java.util.Objects.nonNull;

@Component
public class RegistrationServiceImpl implements RegistrationService {
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final UserValidator validator;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public RegistrationServiceImpl(UserService userService, ConfirmationTokenService confirmationTokenService, UserValidator validator, AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository, EmailService emailService) {
        this.userService = userService;
        this.confirmationTokenService = confirmationTokenService;
        this.validator = validator;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    private static final String CONFIRMATION_API = "http://localhost:8080/web-api/registration/confirm";


    @Override
    @Transactional
    public String register(RegistrationUserRequest registrationUserDto) {
        ValidationResult validationResult = validator
                .validate(registrationUserDto);

        ValidationResult.validOrThrow(validationResult);

        String token = userService.signUpUser(
                User.builder()
                        .firstName(registrationUserDto.firstName())
                        .lastName(registrationUserDto.lastName())
                        .email(registrationUserDto.email())
                        .phoneNumber(registrationUserDto.phoneNumber())
                        .password(registrationUserDto.password())
                        .roles(Set.of(Role.USER))
                        .enabled(false)
                        .accountNonLocked(true)
                        .build()
        );

        String link = UriComponentsBuilder.fromUriString(CONFIRMATION_API)
                .queryParam("token", token)
                .toUriString();

        emailService.send(
                registrationUserDto.email(),
                emailService.buildRegistrationEmail(registrationUserDto.firstName(), link));

        return token;
    }

    @Override
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new RegistrationException("Token not found"));

        User user = userRepository.findById(confirmationToken.userId()).orElseThrow(() ->
                new BadRequestException(format("User with id %s doesn't exist ", confirmationToken.userId()))
        );

        if (nonNull(confirmationToken.confirmedAt())) {
            throw new RegistrationException("Token is already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.expiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new RegistrationException("Token is expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableUser(user);

        return "Confirmed";
    }

    @Override
    public String authenticate(LoginUserRequest loginUserDto) {
        ValidationResult validationResult = validator
                .validate(loginUserDto);

        ValidationResult.validOrThrow(validationResult);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDto.email(),
                        loginUserDto.password()
                )
        );

        User user = userRepository.findByEmail(loginUserDto.email())
                .orElseThrow();

        return jwtService.generateToken(user);
    }
}
