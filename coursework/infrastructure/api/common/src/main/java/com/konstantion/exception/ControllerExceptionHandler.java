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

    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ResponseDto> handleValidationException(HttpServletRequest request, ValidationException e) {
        HttpStatus httpStatus = ExceptionsStatusesExtractor.extractExceptionStatus(e);
        logger.error(buildLoggerMessage(e));
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
        logger.error(buildLoggerMessage(e));
        return ResponseEntity.status(httpStatus).body(
                ResponseDto.builder()
                        .status(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(e.getMessage())
                        .timeStamp(now())
                        .build()
        );
    }

    private String buildLoggerMessage(Exception e) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(String.format("Exception [%s] has been throw out of the class [%s] when executing the method [%s], on the line [%s], with message: [%s]. ", e.getClass().getSimpleName(), e.getStackTrace()[0].getClassName(), e.getStackTrace()[0].getMethodName(), e.getStackTrace()[0].getLineNumber(), e.getMessage()));
        int stackTraceDepth = Math.min(5, e.getStackTrace().length);
        for (int i = 1; i < stackTraceDepth; i++) {
            messageBuilder.append(String.format("Caused by executing the method [%s] in the class [%s] on the line [%s]. ", e.getStackTrace()[i].getMethodName(), e.getStackTrace()[i].getClassName(), e.getStackTrace()[i].getLineNumber()));
        }

        return messageBuilder.toString();
    }
}
