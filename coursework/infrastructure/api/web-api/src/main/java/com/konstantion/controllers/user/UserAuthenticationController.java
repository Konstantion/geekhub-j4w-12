package com.konstantion.controllers.user;

import com.konstantion.authentication.AuthenticationService;
import com.konstantion.dto.authentication.converter.AuthenticationMapper;
import com.konstantion.dto.authentication.dto.AuthenticationResponseDto;
import com.konstantion.dto.user.converter.UserMapper;
import com.konstantion.dto.user.dto.LoginUserRequestDto;
import com.konstantion.response.ResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static com.konstantion.utils.EntityNameConstants.AUTHENTICATION;
import static java.util.Map.of;

@RestController
@RequestMapping("/web-api/authentication")
public record UserAuthenticationController(
        AuthenticationService authenticationService
) {
    private static final UserMapper userMapper = UserMapper.INSTANCE;
    private static final AuthenticationMapper authenticationMapper = AuthenticationMapper.INSTANCE;
    private static final String BEARER_PREFIX = "Bearer ";

    @PostMapping()
    public ResponseEntity<ResponseDto> authenticate(
            @RequestBody LoginUserRequestDto requestDto
    ) {
        AuthenticationResponseDto responseDto = authenticationMapper.toDto(
                authenticationService.authenticate(
                        userMapper.toLoginUserRequest(requestDto)
                ));

        String bearerToken = BEARER_PREFIX + responseDto.token();

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .body(ResponseDto.builder()
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully authenticated")
                        .timeStamp(LocalDateTime.now())
                        .data(of(AUTHENTICATION, responseDto))
                        .build()
                );
    }
}
