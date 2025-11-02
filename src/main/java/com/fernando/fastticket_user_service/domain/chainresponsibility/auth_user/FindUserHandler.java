package com.fernando.fastticket_user_service.domain.chainresponsibility.auth_user;

import com.fernando.fastticket_user_service.application.ports.output.UserPersistencePort;
import com.fernando.fastticket_user_service.domain.exceptions.UserNotFoundException;
import com.fernando.fastticket_user_service.domain.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindUserHandler implements AuthUserHandler {
    private final UserPersistencePort userPersistencePort;
    private AuthUserHandler nextHandler;

    @Override
    public AuthUserHandler setNext(AuthUserHandler handler) {
        this.nextHandler = handler;
        return handler;
    }

    @Override
    public void handle(AuthContext context) {
        User storedUser = userPersistencePort.getUserByEmail(context.getEmail());
        if (storedUser == null) {
            throw new UserNotFoundException("User Not Found: " + context.getEmail());
        }

        context.setStoredUser(storedUser);
        
        if (nextHandler != null) {
            nextHandler.handle(context);
        }
    }
}
