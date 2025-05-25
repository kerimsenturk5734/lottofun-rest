package com.lottofun.lottofunrest.exception;

import com.lottofun.lottofunrest.dto.wrapper.ApiResult;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Handle NotFound exceptions with status 404
    @ExceptionHandler(value = {NotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(RuntimeException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        HttpHeaders headers = new HttpHeaders();

        return handleExceptionInternal(ex, ApiResult.error(ex.getMessage(), status), headers, status, request);
    }

    // Handle Conflict exceptions with status 409
    @ExceptionHandler(value = {ConflictException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        HttpHeaders headers = new HttpHeaders();

        return handleExceptionInternal(ex, ApiResult.error(ex.getMessage(), status), headers, status, request);
    }

    @ExceptionHandler(value = {InsufficientBalanceException.class})
    protected ResponseEntity<Object> handleInsufficientBalance(RuntimeException ex, WebRequest request) {
        HttpStatus status = HttpStatus.PAYMENT_REQUIRED;
        HttpHeaders headers = new HttpHeaders();

        return handleExceptionInternal(ex, ApiResult.error(ex.getMessage(), status), headers, status, request);
    }

    @ExceptionHandler(value = {DrawNotAvailableForPurchaseException.class})
    protected ResponseEntity<Object> handleDrawNotAvailableForPurchase(RuntimeException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        HttpHeaders headers = new HttpHeaders();

        return handleExceptionInternal(ex, ApiResult.error(ex.getMessage(), status), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(field -> field.getField() + ": " + field.getDefaultMessage())
                .toList();

        String message = "Validation failed";
        ApiResult<Object> apiResult = ApiResult.error(message + ": " + String.join(", ", errors), HttpStatus.BAD_REQUEST);

        return handleExceptionInternal(ex, apiResult, headers, HttpStatus.BAD_REQUEST, request);
    }

}
