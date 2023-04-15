package com.konstantion.controllers;

import com.konstantion.dto.mappers.UserMapper;
import com.konstantion.dto.response.ResponseDto;
import com.konstantion.dto.user.UpdateUserDto;
import com.konstantion.dto.user.UserDto;
import com.konstantion.user.User;
import com.konstantion.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/web-api/users")
public record UserController(
        UserService userService
) {
    public static final UserMapper userMapper = UserMapper.INSTANCE;

    @GetMapping("/authorized/roles")
    public ResponseEntity<ResponseDto> getAuthorizedUserRoles(
            @AuthenticationPrincipal User requester
    ) {
        return ResponseEntity.ok(
                ResponseDto.builder()
                        .status(OK)
                        .statusCode(OK.value())
                        .message("Authorized user roles")
                        .data(Map.of("roles", requester.getRoles()))
                        .build()
        );
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ResponseDto> getUser(
            @PathVariable("uuid") UUID uuid
    ) {
        UserDto dto = userMapper.toDto(userService.getUser(uuid));

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .status(OK)
                        .statusCode(OK.value())
                        .message(format("User with id %s", uuid))
                        .data(Map.of("user", dto))
                        .build()
        );
    }

    @GetMapping("/authorized")
    public ResponseEntity<ResponseDto> getAuthorizedUser(
            @AuthenticationPrincipal User user
    ) {
        UserDto dto = userMapper.toDto(user);

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .timeStamp(now())
                        .status(OK)
                        .statusCode(OK.value())
                        .message("Authorized user")
                        .data(Map.of("user", dto))
                        .build());
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ResponseDto> updateUser(
            @PathVariable("uuid") UUID userId,
            @RequestBody UpdateUserDto updateUserDto,
            @AuthenticationPrincipal User user
    ) {
        UserDto dto = userMapper.toDto(userService.editUser(
                userId,
                userMapper.toEntity(updateUserDto),
                user
        ));

        return ResponseEntity.ok(
                ResponseDto.builder()
                        .timeStamp(now())
                        .status(OK)
                        .statusCode(OK.value())
                        .message("User successfully updated")
                        .data(Map.of("user", dto))
                        .build());
    }
}
