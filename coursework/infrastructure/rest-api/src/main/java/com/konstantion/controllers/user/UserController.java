package com.konstantion.controllers.user;

import com.konstantion.controllers.user.converter.UserMapper;
import com.konstantion.controllers.user.dto.UserDto;
import com.konstantion.response.ResponseDto;
import com.konstantion.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.konstantion.utils.EntityNameConstants.USER;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/web-api/users")
public record UserController() {
    private static final UserMapper userMapper = UserMapper.INSTANCE;
    @GetMapping("/authenticated")
    public ResponseDto getAuthenticatedUser(
            @AuthenticationPrincipal User user
    ) {
        UserDto dto = userMapper.toDto(user);

        return ResponseDto.builder()
                .status(OK)
                .statusCode(OK.value())
                .message("Authenticated user")
                .data(Map.of(USER, dto))
                .timeStamp(now())
                .build();
    }
}
