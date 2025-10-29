package com.fernando.fastticket_user_service.domain.chainresponsibility;

import com.fernando.fastticket_user_service.application.ports.output.UserPersistencePort;
import com.fernando.fastticket_user_service.domain.exceptions.UserEmailExistsException;
import com.fernando.fastticket_user_service.domain.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckEmailHandler implements UserRegistrationHandler{
    private final UserPersistencePort userPersistencePort;
    @Override
    public void handle(User user) {
        if(Boolean.TRUE.equals(userPersistencePort.existsByEmail(user.getEmail()))){
            throw new UserEmailExistsException("Email already exists: " + user.getEmail());
        }
    }
}
