package com.fernando.fastticket_user_service.domain.decorator;

import com.fernando.fastticket_user_service.application.ports.input.RegisterUserUseCase;
import com.fernando.fastticket_user_service.domain.models.User;
import com.fernando.fastticket_user_service.domain.services.UserService;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.notification.facade.NotificationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@RequiredArgsConstructor
public class SendEmailAfterRegistrationDecorator implements RegisterUserUseCase {
    private final UserService userService;
    private final NotificationFacade notificationFacade;

    @Override
    public User registerUser(User user) {
        User savedUser = userService.registerUser(user);
        notificationFacade.notifyUser(savedUser);
        return savedUser;
    }
}
