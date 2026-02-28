package ru.yandex.practicum.telemetry.collector.handlers;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidation(RuntimeException e) {
        String message = e.getMessage();
        log.warn("Validation error occurred in collector: {}", message);
        return new ApiError(message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleUncaught(RuntimeException e) {
        String message = e.getMessage();
        log.warn("Unexpected error occurred in collector: {}", message);
        return new ApiError(message);
    }

    record ApiError(String error) {
    }
}
