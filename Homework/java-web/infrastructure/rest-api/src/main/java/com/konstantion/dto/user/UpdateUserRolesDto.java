package com.konstantion.dto.user;

import java.util.List;

public record UpdateUserRolesDto(
        List<String> roles
) {
}
