package com.fernando.fastticket_user_service.domain.services;

import com.fernando.fastticket_user_service.application.ports.input.RegisterUserUseCase;
import com.fernando.fastticket_user_service.application.ports.output.UserPersistencePort;
import com.fernando.fastticket_user_service.domain.models.User;
import com.fernando.fastticket_user_service.domain.chainresponsibility.UserRegistrationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service("userServiceImpl")
public class UserService implements RegisterUserUseCase {
    private final UserPersistencePort userPersistencePort;
    private final List<UserRegistrationHandler> handlers;

    @Override
    public User registerUser(User user) {
        handlers.forEach(handler -> handler.handle(user));
        return userPersistencePort.registerUser(user);
    }
}
