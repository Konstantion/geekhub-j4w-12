package com.konstantion.controllers.user;

import com.konstantion.dto.user.converter.UserMapper;
import com.konstantion.dto.user.dto.UserDto;
import com.konstantion.response.ResponseDto;
import com.konstantion.user.User;
import com.konstantion.user.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.konstantion.utils.EntityNameConstants.USER;
import static com.konstantion.utils.EntityNameConstants.USERS;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/web-api/users")
public record UserController(
        UserService userService
) {
    private static final UserMapper userMapper = UserMapper.INSTANCE;

    @GetMapping()
    public ResponseDto getAllActiveUsers() {
        List<UserDto> dtos = userMapper.toDto(userService.getAll());

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("All active users successfully returned")
                .timeStamp(now())
                .data(Map.of(USERS, dtos))
                .build();
    }

    @GetMapping("/{id}")
    public ResponseDto getUserById(
            @PathVariable("id") UUID id,
            @AuthenticationPrincipal User user
    ) {
        UserDto dto = userMapper.toDto(userService.getUserById(id));

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message(format("User with id %s successfully returned", id))
                .timeStamp(now())
                .data(Map.of(USER, dto))
                .build();
    }
}
