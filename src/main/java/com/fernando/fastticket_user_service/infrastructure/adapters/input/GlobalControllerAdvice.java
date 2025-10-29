package com.fernando.fastticket_user_service.infrastructure.adapters.input;

import com.fernando.fastticket_user_service.domain.exceptions.RolNotFoundException;
import com.fernando.fastticket_user_service.domain.exceptions.UserEmailExistsException;
import com.fernando.fastticket_user_service.infrastructure.adapters.input.models.reponses.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.validation.BindValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.fernando.fastticket_user_service.domain.enums.ErrorType.FUNCTIONAL;
import static com.fernando.fastticket_user_service.domain.enums.ErrorType.SYSTEM;
import static com.fernando.fastticket_user_service.infrastructure.utils.ErrorCatalog.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserEmailExistsException.class)
    public ErrorResponse handleEmailAlreadyExistsException(UserEmailExistsException e){
        logException(FUNCTIONAL.name(), USER_EMAIL_EXISTS.getCode(), e.getMessage());
        return ErrorResponse.builder()
                .code(USER_EMAIL_EXISTS.getCode())
                .type(FUNCTIONAL)
                .message(USER_EMAIL_EXISTS.getMessage())
                .details(Collections.singletonList(e.getMessage()))
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleEmailAlreadyExistsException(MethodArgumentNotValidException e){
        logException(FUNCTIONAL.name(), USER_BAD_PARAMETER.getCode(), e.getMessage());
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();
        return ErrorResponse.builder()
                .code(USER_BAD_PARAMETER.getCode())
                .type(FUNCTIONAL)
                .message(USER_BAD_PARAMETER.getMessage())
                .details(errors)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RolNotFoundException.class)
    public ErrorResponse handleRolNotFoundException(RolNotFoundException e){
        logException(FUNCTIONAL.name(), ROL_NOT_FOUND.getCode(), e.getMessage());
        return ErrorResponse.builder()
                .code(ROL_NOT_FOUND.getCode())
                .type(FUNCTIONAL)
                .message(ROL_NOT_FOUND.getMessage())
                .details(Collections.singletonList(e.getMessage()))
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e) {
        logException(SYSTEM.name(), USER_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        return ErrorResponse.builder()
                .code(USER_INTERNAL_SERVER_ERROR.getCode())
                .type(SYSTEM)
                .message(USER_INTERNAL_SERVER_ERROR.getMessage())
                .details(Collections.singletonList(e.getMessage()))
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    private void logException(String type, String code, String message){
        switch (type){
            case "FUNCTIONAL"-> log.warn("⚠️ Warning ({}): {}", code, message);
            case "SYSTEM"-> log.error("❌ Error ({}): {}", code, message);
            default -> log.error("❌ Error (Unknow): Type not found.");
        }
    }
}
