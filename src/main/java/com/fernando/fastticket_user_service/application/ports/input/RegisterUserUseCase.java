package com.fernando.fastticket_user_service.application.ports.input;

import com.fernando.fastticket_user_service.domain.models.User;

public interface RegisterUserUseCase {
    User registerUser(User user);
}
