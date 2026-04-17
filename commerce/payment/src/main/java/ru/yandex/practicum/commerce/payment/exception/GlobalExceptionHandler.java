package ru.yandex.practicum.commerce.payment.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.commerce.exception.NoOrderFoundException;
import ru.yandex.practicum.commerce.exception.NotEnoughInfoInOrderToCalculateException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Exception handleNoOrderFound(final NoOrderFoundException e) {
        String reason = "No order found";
        log.error(reason + ". " + e.getMessage());
        return e;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Exception handleNotEnoughInfoToCalculate(final NotEnoughInfoInOrderToCalculateException e) {
        String reason = "Not enough info to calculate";
        log.error(reason + ". " + e.getMessage());
        return e;
    }

    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Exception handleValidation(final Exception e) {
        String message = e.getMessage();
        log.warn("Validation error: {}", message);
        return e;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Exception handleUncaught(final Exception e) {
        String reason = "Internal server error";
        log.error(reason + ". " + e.getMessage());
        return e;
    }
}
