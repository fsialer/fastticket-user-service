package com.fernando.fastticket_user_service.domain.services;

import com.fernando.fastticket_user_service.application.ports.input.CheckCredentialUseCase;
import com.fernando.fastticket_user_service.domain.chainresponsibility.AuthContext;
import com.fernando.fastticket_user_service.domain.chainresponsibility.CheckEmailConfirmHandler;
import com.fernando.fastticket_user_service.domain.chainresponsibility.CheckPasswordHandler;
import com.fernando.fastticket_user_service.domain.chainresponsibility.FindUserHandler;
import com.fernando.fastticket_user_service.domain.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements CheckCredentialUseCase {

    private final FindUserHandler findUserHandler;
    private final CheckEmailConfirmHandler checkEmailConfirmHandler;
    private final CheckPasswordHandler checkPasswordHandler;

    @Override
    public User checkCredential(User user) {
        AuthContext context = AuthContext.builder().email(user.getEmail()).password(user.getPassword()).build();
        findUserHandler
                .setNext(checkEmailConfirmHandler)
                .setNext(checkPasswordHandler);
        findUserHandler.handle(context);
        return context.getStoredUser();
    }
}
