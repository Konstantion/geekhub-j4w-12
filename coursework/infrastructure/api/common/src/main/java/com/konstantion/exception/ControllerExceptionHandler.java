package com.konstantion.exception;

import com.konstantion.response.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import static java.time.LocalDateTime.now;

@ControllerAdvice
public record ControllerExceptionHandler() {

    public static Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ResponseDto> handleValidationException(HttpServletRequest request, ValidationException e) {
        HttpStatus httpStatus = ExceptionsStatusesExtractor.extractExceptionStatus(e);
        return ResponseEntity.status(httpStatus).body(
                ResponseDto.builder()
                        .status(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(e.getMessage())
                        .data(e.getValidationErrors())
                        .timeStamp(now())
                        .build()
        );
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    public String handleNoHandlerFoundException(NoHandlerFoundException e) {
        return "error/404";
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ResponseDto> handleExceptionWithoutBody(HttpServletRequest request, Exception e) {
        HttpStatus httpStatus = ExceptionsStatusesExtractor.extractExceptionStatus(e);
        return ResponseEntity.status(httpStatus).body(
                ResponseDto.builder()
                        .status(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(e.getMessage())
                        .timeStamp(now())
                        .build()
        );
    }
}
