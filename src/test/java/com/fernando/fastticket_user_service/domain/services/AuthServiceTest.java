package com.fernando.fastticket_user_service.domain.services;

import com.fernando.fastticket_user_service.domain.chainresponsibility.CheckEmailConfirmHandler;
import com.fernando.fastticket_user_service.domain.chainresponsibility.CheckPasswordHandler;
import com.fernando.fastticket_user_service.domain.chainresponsibility.FindUserHandler;
import com.fernando.fastticket_user_service.domain.exceptions.UserNotFoundException;
import com.fernando.fastticket_user_service.domain.models.User;
import com.fernando.fastticket_user_service.utils.TestUtilUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private FindUserHandler findUserHandler;

    @Mock
    private CheckEmailConfirmHandler checkEmailConfirmHandler;

    @Mock
    private CheckPasswordHandler checkPasswordHandler;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("When Check Credential Successfully Expect User Returned")
    void When_CheckCredentialSuccessfully_Expect_UserReturned() {
        // Arrange
        User inputUser = TestUtilUser.mockUser();
        User storedUser = TestUtilUser.mockUser();
        
        when(findUserHandler.setNext(any())).thenReturn(checkEmailConfirmHandler);
        when(checkEmailConfirmHandler.setNext(any())).thenReturn(checkPasswordHandler);
        doNothing().when(findUserHandler).handle(any());
        
        // Act
        User result = authService.checkCredential(inputUser);
        
        // Assert
        verify(findUserHandler, times(1)).setNext(checkEmailConfirmHandler);
        verify(checkEmailConfirmHandler, times(1)).setNext(checkPasswordHandler);
        verify(findUserHandler, times(1)).handle(any());
    }

    @Test
    @DisplayName("When Handler Throws Exception Expect Exception Propagated")
    void When_HandlerThrowsException_Expect_ExceptionPropagated() {
        // Arrange
        User inputUser = TestUtilUser.mockUser();
        
        when(findUserHandler.setNext(any())).thenReturn(checkEmailConfirmHandler);
        when(checkEmailConfirmHandler.setNext(any())).thenReturn(checkPasswordHandler);
        doThrow(new UserNotFoundException("User not found")).when(findUserHandler).handle(any());
        
        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> authService.checkCredential(inputUser));
        
        verify(findUserHandler, times(1)).handle(any());
    }

    @Test
    @DisplayName("When Chain Setup Correctly Expect Proper Order")
    void When_ChainSetupCorrectly_Expect_ProperOrder() {
        // Arrange
        User inputUser = TestUtilUser.mockUser();
        
        when(findUserHandler.setNext(checkEmailConfirmHandler)).thenReturn(checkEmailConfirmHandler);
        when(checkEmailConfirmHandler.setNext(checkPasswordHandler)).thenReturn(checkPasswordHandler);
        
        // Act
        authService.checkCredential(inputUser);
        
        // Assert - Verify chain setup order
        verify(findUserHandler).setNext(checkEmailConfirmHandler);
        verify(checkEmailConfirmHandler).setNext(checkPasswordHandler);
    }
}