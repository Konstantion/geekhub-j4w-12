package com.konstantion.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class ExceptionsStatusesExtractor {
    private ExceptionsStatusesExtractor() {
    }

    public static HttpStatus extractExceptionStatus(Exception e) {
        if (e instanceof ValidationException) {
            return HttpStatus.UNPROCESSABLE_ENTITY;
        } else if (e instanceof BadRequestException) {
            return HttpStatus.BAD_REQUEST;
        } else if (e instanceof ForbiddenException) {
            return HttpStatus.FORBIDDEN;
        } else if (e instanceof AuthenticationException) {
            return HttpStatus.UNAUTHORIZED;
        } else if (e instanceof DataAccessException) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }
}
