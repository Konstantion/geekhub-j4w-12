package com.konstantion.controllers;

import com.konstantion.dto.mappers.UserMapper;
import com.konstantion.dto.response.ResponseDto;
import com.konstantion.dto.user.LoginUserDto;
import com.konstantion.dto.user.RegistrationUserDto;
import com.konstantion.dto.user.RestoreUserRequestDto;
import com.konstantion.ragistration.RegistrationService;
import com.konstantion.user.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;

import static java.util.Map.of;

@RestController
@RequestMapping("/web-api/registration")
public record RegistrationController(
        RegistrationService registrationService,
        UserService userService
) {
    public static final UserMapper userMapper = UserMapper.INSTANCE;
    public static final String BEARER_PREFIX = "Bearer ";

    @PostMapping
    public ResponseDto register(
            @RequestBody RegistrationUserDto registrationUserDto
    ) {
        String token = registrationService.register(
                userMapper.toEntity(registrationUserDto)
        );
        return ResponseDto.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Confirmation token will be send to your email in 1 min, if you don't get it please try again!")
                .timeStamp(LocalDateTime.now())
                .build();

    }

    @GetMapping("/confirm")
    public RedirectView confirm(@RequestParam("token") String token) {
        registrationService.confirmToken(token);
        return new RedirectView("/");
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(
            @RequestBody LoginUserDto loginUserDto
    ) {
        final String jwtToken = registrationService.authenticate(
                userMapper.toEntity(loginUserDto)
        );
        final String authorizationToken = BEARER_PREFIX + jwtToken;
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, authorizationToken)
                .body(ResponseDto.builder()
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully logged in")
                        .timeStamp(LocalDateTime.now())
                        .data(of("jwtToken", jwtToken))
                        .build()
                );
    }

    @PostMapping("/restore")
    public ResponseDto restoreUser(
            @RequestBody RestoreUserRequestDto requestDto
    ) {
        String token = registrationService.restorePassword(
                userMapper.toRestoreUserRequest(requestDto)
        );

        return ResponseDto.builder()
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .message("Email with link to confirm your restore will be sent to your email in 1 min, if you don't get it please try again!")
                .timeStamp(LocalDateTime.now())
                .build();
    }

    @GetMapping("/restore/confirm")
    public RedirectView restoreUser(
            @RequestParam("token") String token
    ) {
        registrationService.confirmRestore(token);
        return new RedirectView("/");
    }
}
