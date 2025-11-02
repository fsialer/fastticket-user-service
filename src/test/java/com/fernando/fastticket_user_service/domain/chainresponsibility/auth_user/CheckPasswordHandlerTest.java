package com.fernando.fastticket_user_service.domain.chainresponsibility.auth_user;

import com.fernando.fastticket_user_service.domain.exceptions.PasswordInvalidedException;
import com.fernando.fastticket_user_service.domain.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckPasswordHandlerTest {
    private CheckPasswordHandler handler;

    @Mock
    private AuthUserHandler nextHandler;

    @Mock
    private User user;

    private AuthContext context;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        handler = new CheckPasswordHandler(passwordEncoder);
        context = new AuthContext();
        context.setEmail("test@example.com");
        context.setPassword("testPassword");
        context.setStoredUser(user);
    }

    @Test
    @DisplayName("When Password Matches Expect NextHandle")
    void When_PasswordMatches_Expect_NextHandle() {
        when(user.getPassword()).thenReturn("encodedPassword");
        when(passwordEncoder.matches("testPassword", "encodedPassword")).thenReturn(true);
        handler.setNext(nextHandler);

        handler.handle(context);

        verify(nextHandler).handle(context);
    }

    @Test
    @DisplayName("Expect Password InvalidedException When Password Not Matches")
    void Expect_PasswordInvalidedException_When_PasswordNotMatches() {
        when(user.getPassword()).thenReturn("encodedPassword");
        when(passwordEncoder.matches("testPassword", "encodedPassword")).thenReturn(false);

        PasswordInvalidedException exception = assertThrows(
                PasswordInvalidedException.class,
                () -> handler.handle(context)
        );

        assertEquals("Password don´t match.", exception.getMessage());
    }

    @Test
    @DisplayName("Expect Not Call NextHandler When HandleNext Is Null")
    void Expect_NotCallNextHandler_When_HandleNextIsNull() {
        when(user.getPassword()).thenReturn("encodedPassword");
        when(passwordEncoder.matches("testPassword", "encodedPassword")).thenReturn(true);

        assertDoesNotThrow(() -> handler.handle(context));
    }
}
