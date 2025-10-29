package com.fernando.fastticket_user_service.application.ports.output;

import com.fernando.fastticket_user_service.domain.models.User;

public interface NotificationPort {
    void send(User user);
}
