package com.konstantion.controllers;

import com.konstantion.response.Response;
import com.konstantion.user.User;
import com.konstantion.user.UserService;
import com.konstantion.user.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/authorized/roles")
    public ResponseEntity<Response> getAuthorizedUserRoles(
            @AuthenticationPrincipal User requester
    ) {
        return ResponseEntity.ok(
                Response.builder()
                        .status(OK)
                        .statusCode(OK.value())
                        .message("Authorized user roles")
                        .data(Map.of("roles", requester.getRoles()))
                        .build()
        );
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Response> getUser(
            @PathVariable("uuid") UUID uuid,
            @AuthenticationPrincipal User requester
    ) {
        UserDto dto = userService.getUser(uuid, requester);

        return ResponseEntity.ok(
                Response.builder()
                        .status(OK)
                        .statusCode(OK.value())
                        .message(format("User with id %s", uuid))
                        .data(Map.of("user", dto))
                        .build()
        );
    }

    @GetMapping("/authorized")
    public ResponseEntity<Response> getAuthorizedUser(
            @AuthenticationPrincipal User user
    ) {
        UserDto dto = userService.getUser(user.getId(), user);

        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .status(OK)
                        .statusCode(OK.value())
                        .message("Authorized user roles")
                        .data(Map.of("user", dto))
                        .build());
    }
}
