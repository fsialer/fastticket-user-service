package com.fernando.fastticket_user_service.domain.chainresponsibility;

import com.fernando.fastticket_user_service.domain.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EncoderPasswordHandler implements UserRegistrationHandler{
    private final PasswordEncoder passwordEncoder;

    @Override
    public void handle(User user) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
}
