package com.konstantion.controllers;

import com.konstantion.dto.mappers.UserMapper;
import com.konstantion.dto.response.ResponseDto;
import com.konstantion.dto.user.LoginUserDto;
import com.konstantion.dto.user.RegistrationUserDto;
import com.konstantion.ragistration.RegistrationService;
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
        RegistrationService registrationService
) {
    public static final UserMapper userMapper = UserMapper.INSTANCE;

    @PostMapping
    public ResponseEntity<ResponseDto> register(
            @RequestBody RegistrationUserDto registrationUserDto
    ) {
        String token = registrationService.register(
                userMapper.toEntity(registrationUserDto)
        );
        return ResponseEntity.ok(
                ResponseDto.builder()
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Confirmation token will be send to your email in 1 min, if you don't get it please try again!")
                        .timeStamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("confirm")
    public RedirectView confirm(@RequestParam("token") String token) {
        registrationService.confirmToken(token);
        return new RedirectView("/");
    }

    @PostMapping("login")
    public ResponseEntity<ResponseDto> login(
            @RequestBody LoginUserDto loginUserDto
    ) {
        String jwtToken = registrationService.authenticate(
                userMapper.toEntity(loginUserDto)
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .body(ResponseDto.builder()
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully logged in")
                        .timeStamp(LocalDateTime.now())
                        .data(of("jwtToken", jwtToken))
                        .build()
                );
    }
}
