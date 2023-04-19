package com.konstantion.controllers;

import com.konstantion.dto.mappers.UserMapper;
import com.konstantion.dto.response.ResponseDto;
import com.konstantion.dto.user.CreateUserRequestDto;
import com.konstantion.dto.user.UpdateUserRolesDto;
import com.konstantion.dto.user.UserDto;
import com.konstantion.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

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

    @DeleteMapping("/users/{id}")
    public ResponseDto deleteUser(
            @PathVariable("id") UUID id
    ) {
        UserDto dto = userMapper.toDto(userService.delete(id));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .timeStamp(now())
                .message("User deleted successfully")
                .data(Map.of("user", dto))
                .build();
    }

    @PutMapping("/users/{uuid}/enable")
    public ResponseEntity<ResponseDto> enableUser(
            @PathVariable("uuid") UUID userId
    ) {
        UUID uuid = userService.enableUser(userId);

        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .message("User enabled")
                .status(OK)
                .data(Map.of("uuid", uuid))
                .build()
        );
    }

    @PutMapping("/users/{uuid}/disable")
    public ResponseEntity<ResponseDto> disableUser(
            @PathVariable("uuid") UUID userId
    ) {
        UUID uuid = userService.disableUser(userId);

        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .message("User disabled")
                .status(OK)
                .data(Map.of("uuid", uuid))
                .build()
        );
    }

    @PutMapping("/users/{uuid}/roles")
    public ResponseEntity<ResponseDto> updateUserRoles(
            @PathVariable("uuid") UUID userId,
            @RequestBody UpdateUserRolesDto updateUserRolesDto
    ) {
        UserDto user = userMapper.toDto(userService.updateUserRoles(
                userId,
                userMapper.toEntity(updateUserRolesDto)
        ));

        return ResponseEntity.ok(ResponseDto.builder()
                .timeStamp(now())
                .statusCode(OK.value())
                .message("User roles updated")
                .status(OK)
                .data(Map.of("user", user))
                .build()
        );
    }
}
