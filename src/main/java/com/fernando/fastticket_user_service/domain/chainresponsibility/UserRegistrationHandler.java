package com.fernando.fastticket_user_service.domain.chainresponsibility;

import com.fernando.fastticket_user_service.domain.models.User;

public interface UserRegistrationHandler {
    void handle(User user);
}
