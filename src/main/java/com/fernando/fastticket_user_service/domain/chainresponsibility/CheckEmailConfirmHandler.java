package com.fernando.fastticket_user_service.domain.chainresponsibility;

import com.fernando.fastticket_user_service.domain.exceptions.EmailNotConfirmedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckEmailConfirmHandler implements AuthUserHandler{
    private AuthUserHandler nextHandler;
    @Override
    public AuthUserHandler setNext(AuthUserHandler handler) {
        this.nextHandler = handler;
        return handler;
    }

    @Override
    public void handle(AuthContext context) {
        if(!context.getStoredUser().isConfirmEmail()){
            throw new EmailNotConfirmedException("Email "+context.getEmail()+" couldn´t be confirm.");
        }
        if (nextHandler != null) {
            nextHandler.handle(context);
        }
    }
}
