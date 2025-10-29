package com.fernando.fastticket_user_service.domain.exceptions;

public class RolNotFoundException extends RuntimeException{
    public RolNotFoundException(String message){
        super(message);
    }
}
