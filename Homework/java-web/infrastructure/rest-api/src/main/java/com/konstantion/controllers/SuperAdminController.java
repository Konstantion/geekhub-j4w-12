package com.konstantion.controllers;

import com.konstantion.dto.mappers.UserMapper;
import com.konstantion.dto.response.ResponseDto;
import com.konstantion.dto.user.CreateUserRequestDto;
import com.konstantion.dto.user.UserDto;
import com.konstantion.user.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/super-admin-api")
public record SuperAdminController(
        UserService userService
) {
    private static final UserMapper userMapper = UserMapper.INSTANCE;

    @PostMapping("/users/admins")
    public ResponseDto createAdmin(
            @RequestBody CreateUserRequestDto request
    ) {
        UserDto dto = userMapper.toDto(userService.createAdmin(
                userMapper.toCreateUserRequest(request)
        ));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message("Admin successfully created")
                .data(Map.of("user", dto))
                .build();
    }

    @PostMapping("/users/moderators")
    public ResponseDto createModerator(
            @RequestBody CreateUserRequestDto request
    ) {
        UserDto dto = userMapper.toDto(userService.createModerator(
                userMapper.toCreateUserRequest(request)
        ));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message("Moderator successfully created")
                .data(Map.of("user", dto))
                .build();
    }
}
