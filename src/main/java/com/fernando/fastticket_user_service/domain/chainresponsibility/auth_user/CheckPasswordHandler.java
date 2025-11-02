package com.fernando.fastticket_user_service.domain.chainresponsibility.auth_user;

import com.fernando.fastticket_user_service.domain.exceptions.PasswordInvalidedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckPasswordHandler implements AuthUserHandler{

    private final PasswordEncoder passwordEncoder;
    private AuthUserHandler nextHandler;

    @Override
    public AuthUserHandler setNext(AuthUserHandler handler) {
        this.nextHandler = handler;
        return handler;
    }

    @Override
    public void handle(AuthContext context) {
        if(!passwordEncoder.matches(context.getPassword(),context.getStoredUser().getPassword())){
            throw new PasswordInvalidedException("Password don´t match.");
        }
        if (nextHandler != null) {
            nextHandler.handle(context);
        }

    }
}
