package com.konstantion.exception;

import org.springframework.http.HttpStatus;

public class ExceptionsStatusesExtractor {
    private ExceptionsStatusesExtractor() {
    }

    public static HttpStatus extractExceptionStatus(Exception e) {
        if (e instanceof ValidationException) {
            return HttpStatus.UNPROCESSABLE_ENTITY;
        } else if (e instanceof BadRequestException) {
            return HttpStatus.BAD_REQUEST;
        }  else if (e instanceof ForbiddenException) {
            return HttpStatus.FORBIDDEN;
        } /*else if (e instanceof RegistrationException) {
            return HttpStatus.CONFLICT;
        }*/ else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
