package com.fernando.fastticket_user_service.infrastructure.adapters.output.notification.facade;

import com.fernando.fastticket_user_service.application.ports.output.NotificationPort;
import com.fernando.fastticket_user_service.domain.models.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class NotificationFacade {

    private final List<NotificationPort> notificationPort;

    public void notifyUser(User user) {
        for (NotificationPort port : notificationPort) {
            port.send(user);
        }
    }
}
