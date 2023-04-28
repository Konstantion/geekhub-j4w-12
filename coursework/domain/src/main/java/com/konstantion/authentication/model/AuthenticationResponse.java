package com.konstantion.authentication.model;

import org.springframework.security.core.userdetails.UserDetails;

public record AuthenticationResponse(
        String token,
        UserDetails userDetails,
        String type
) {
}
