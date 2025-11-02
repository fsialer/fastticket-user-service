package com.fernando.fastticket_user_service.domain.exceptions;

public class PasswordInvalidedException extends RuntimeException{
    public PasswordInvalidedException(String message){
        super(message);
    }
}
