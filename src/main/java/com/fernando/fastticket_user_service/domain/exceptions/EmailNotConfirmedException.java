package com.fernando.fastticket_user_service.domain.exceptions;

public class EmailNotConfirmedException extends RuntimeException{
    public EmailNotConfirmedException(String message){
        super(message);
    }
}
