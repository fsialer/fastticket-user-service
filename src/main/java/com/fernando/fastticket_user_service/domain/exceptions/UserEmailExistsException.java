package com.fernando.fastticket_user_service.domain.exceptions;

public class UserEmailExistsException extends RuntimeException{
    public UserEmailExistsException(String message){
        super(message);
    }
}
