package com.konstantion.user.model;

import java.util.List;

public record UpdateUserRolesRequest(
        List<String> roles
) {
}
