package com.konstantion.exceptions;

import com.konstantion.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.time.LocalDateTime.now;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<Response> handleValidationException(HttpServletRequest request, ValidationException e) {
        HttpStatus httpStatus = ExceptionsStatusesExtractor.extractExceptionStatus(e);
        return ResponseEntity.status(httpStatus).body(
                Response.builder()
                        .status(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(e.getMessage())
                        .data(e.getValidationErrors())
                        .timeStamp(now())
                        .build()
        );
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Response> handleExceptionWithoutBody(HttpServletRequest request, Exception e) {
        HttpStatus httpStatus = ExceptionsStatusesExtractor.extractExceptionStatus(e);
        return ResponseEntity.status(httpStatus).body(
                Response.builder()
                        .status(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(e.getMessage())
                        .timeStamp(now())
                        .build()
        );
    }
}
