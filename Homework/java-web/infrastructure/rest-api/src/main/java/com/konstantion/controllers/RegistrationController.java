package com.konstantion.controllers;

import com.konstantion.ragistration.RegistrationService;
import com.konstantion.response.Response;
import com.konstantion.user.dto.LoginUserDto;
import com.konstantion.user.dto.RegistrationUserDto;
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
    @PostMapping
    public ResponseEntity<Response> register(
            @RequestBody RegistrationUserDto registrationUserDto
    ) {
        String token = registrationService.register(registrationUserDto);
        return ResponseEntity.ok(
                Response.builder()
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
    public ResponseEntity<Response> login(@RequestBody LoginUserDto loginUserDto) {
        String jwtToken = registrationService.authenticate(loginUserDto);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .body(Response.builder()
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully logged in")
                        .timeStamp(LocalDateTime.now())
                        .data(of("jwtToken", jwtToken))
                        .build()
                );
    }

    @GetMapping
    public ResponseEntity<Response> testEndPoint() {
        return ResponseEntity.ok(
                Response.builder()
                        .message("Welcome to the secret and point")
                        .build()
        );
    }
}
