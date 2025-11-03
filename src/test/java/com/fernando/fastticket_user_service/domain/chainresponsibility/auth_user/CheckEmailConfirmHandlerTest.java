package com.fernando.fastticket_user_service.domain.chainresponsibility.auth_user;

import com.fernando.fastticket_user_service.domain.models.User;
import com.fernando.fastticket_user_service.domain.chainresponsibility.AuthContext;
import com.fernando.fastticket_user_service.domain.chainresponsibility.AuthUserHandler;
import com.fernando.fastticket_user_service.domain.chainresponsibility.CheckEmailConfirmHandler;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.persistence.models.UserEntity;
import com.fernando.fastticket_user_service.domain.exceptions.EmailNotConfirmedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckEmailConfirmHandlerTest {

    private CheckEmailConfirmHandler handler;
    
    @Mock
    private AuthUserHandler nextHandler;
    
    @Mock
    private UserEntity userEntity;

    @Mock
    private User user;
    
    private AuthContext context;

    @BeforeEach
    void setUp() {
        handler = new CheckEmailConfirmHandler();
        context = new AuthContext();
        context.setEmail("test@example.com");
        context.setStoredUser(user);
    }

    @Test
    @DisplayName("When Email Is Confirmed Expect NextHandle")
    void When_EmailIsConfirmed_Expect_True() {
        when(user.isConfirmEmail()).thenReturn(true);
        handler.setNext(nextHandler);
        handler.handle(context);
        verify(nextHandler).handle(context);
    }

    @Test
    @DisplayName("Expect EmailNotConfirmedException When EmaiConfirm Is False")
    void Expect_EmailNotConfirmedException_When_EmailConfirmIsFalse() {
        when(user.isConfirmEmail()).thenReturn(false);

        EmailNotConfirmedException exception = assertThrows(
                EmailNotConfirmedException.class,
            () -> handler.handle(context)
        );

        assertEquals("Email "+context.getEmail()+" couldn´t be confirm.", exception.getMessage());
    }

    @Test
    @DisplayName("Expect Not Call NextHandler When HandleNext Is Null")
    void Expect_NotCallNextHandler_When_HandleNextIsNull() {
        when(user.isConfirmEmail()).thenReturn(true);

        assertDoesNotThrow(() -> handler.handle(context));
    }
}