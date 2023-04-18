package com.konstantion.controllers.user;

import com.konstantion.dto.user.converter.UserMapper;
import com.konstantion.dto.user.dto.CreateUserRequestDto;
import com.konstantion.dto.user.dto.UpdateUserRequestDto;
import com.konstantion.dto.user.dto.UserDto;
import com.konstantion.response.ResponseDto;
import com.konstantion.user.Role;
import com.konstantion.user.User;
import com.konstantion.user.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.konstantion.utils.EntityNameConstants.*;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/admin-api/users")
public record AdminUserController(
        UserService userService
) {
    private static final UserMapper userMapper = UserMapper.INSTANCE;

    @GetMapping()
    public ResponseDto getAllUsers() {
        List<UserDto> dtos = userMapper.toDto(userService.getAll(false));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("All users successfully returned")
                .timeStamp(now())
                .data(Map.of(USERS, dtos))
                .build();
    }

    @PostMapping("/waiters")
    public ResponseDto createWaiter(
            @RequestBody CreateUserRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        UserDto dto = userMapper.toDto(
                userService.createWaiter(
                        userMapper.toCreateUserRequest(requestDto),
                        user
                )
        );

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("Waiter successfully created")
                .timeStamp(now())
                .data(Map.of(TABLE, dto))
                .build();
    }

    @PostMapping("/admins")
    public ResponseDto createAdmin(
            @RequestBody CreateUserRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        UserDto dto = userMapper.toDto(
                userService.createAdmin(
                        userMapper.toCreateUserRequest(requestDto),
                        user
                )
        );

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("Admin successfully created")
                .timeStamp(now())
                .data(Map.of(TABLE, dto))
                .build();
    }

    @GetMapping("/waiters")
    public ResponseDto getAllWaiters() {
        List<UserDto> dto = userMapper.toDto(
                userService.getAll(false, Role.WAITER)
        );

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("All waiters successfully returned")
                .timeStamp(now())
                .data(Map.of(TABLE, dto))
                .build();
    }

    @GetMapping("/admins")
    public ResponseDto getAllAdmins() {
        List<UserDto> dto = userMapper.toDto(
                userService.getAll(false, Role.ADMIN)
        );

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("All admins successfully returned")
                .timeStamp(now())
                .data(Map.of(TABLE, dto))
                .build();
    }


    @DeleteMapping("/{id}")
    public ResponseDto deleteUserById(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal User user
    ) {
        UserDto dto = userMapper.toDto(userService.delete(id, user));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message(format("User with id %s successfully deleted", id))
                .timeStamp(now())
                .data(Map.of(ORDER, dto))
                .build();
    }

    @PutMapping("/{id}")
    public ResponseDto updateUserById(
            @PathVariable("id") UUID id,
            @RequestBody UpdateUserRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        UserDto dto = userMapper.toDto(userService.update(
                id,
                userMapper.toUpdateUserRequest(requestDto),
                user
        ));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message(format("User with id %s successfully updated", id))
                .timeStamp(now())
                .data(Map.of(TABLE, dto))
                .build();
    }

    @PutMapping("/{id}/activate")
    public ResponseDto activateUserById(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal User user
    ) {
        UserDto dto = userMapper.toDto(userService.activate(id, user));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message(format("User with id %s successfully activated", id))
                .timeStamp(now())
                .data(Map.of(TABLE, dto))
                .build();
    }

    @PutMapping("/{id}/deactivate")
    public ResponseDto deactivateUserById(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal User user
    ) {
        UserDto dto = userMapper.toDto(userService.deactivate(id, user));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message(format("User with id %s successfully deactivated", id))
                .timeStamp(now())
                .data(Map.of(TABLE, dto))
                .build();
    }
}
