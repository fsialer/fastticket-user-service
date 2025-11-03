package com.fernando.fastticket_user_service.domain.chainresponsibility.auth_user;

import com.fernando.fastticket_user_service.application.ports.output.UserPersistencePort;
import com.fernando.fastticket_user_service.domain.exceptions.UserNotFoundException;
import com.fernando.fastticket_user_service.domain.models.User;
import com.fernando.fastticket_user_service.domain.chainresponsibility.AuthContext;
import com.fernando.fastticket_user_service.domain.chainresponsibility.AuthUserHandler;
import com.fernando.fastticket_user_service.domain.chainresponsibility.FindUserHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindUserHandlerTest {
    private FindUserHandler handler;

    @Mock
    private AuthUserHandler nextHandler;

    @Mock
    private User user;

    private AuthContext context;

    @Mock
    private UserPersistencePort userPersistencePort;

    @BeforeEach
    void setUp() {
        handler = new FindUserHandler(userPersistencePort);
        context = new AuthContext();
        context.setEmail("test@example.com");
        context.setPassword("testPassword");
        context.setStoredUser(user);
    }

    @Test
    @DisplayName("When Email Is Confirm Expect NextHandle")
    void When_EmailIsConfirm_Expect_NextHandle() {
        when(userPersistencePort.getUserByEmail(anyString())).thenReturn(user);
        handler.setNext(nextHandler);

        handler.handle(context);

        verify(nextHandler).handle(context);
    }

    @Test
    @DisplayName("Expect UserNotFoundException When Email Is Not Confirm")
    void Expect_UserNotFoundException_When_EmailIsNotConfirm() {
        when(userPersistencePort.getUserByEmail(anyString())).thenReturn(null);

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> handler.handle(context)
        );

        assertEquals("User Not Found: " + context.getEmail(), exception.getMessage());
    }

    @Test
    @DisplayName("Expect Not Call NextHandler When HandleNext Is Null")
    void Expect_NotCallNextHandler_When_HandleNextIsNull() {
        when(userPersistencePort.getUserByEmail(anyString())).thenReturn(user);
        assertDoesNotThrow(() -> handler.handle(context));
    }

}
