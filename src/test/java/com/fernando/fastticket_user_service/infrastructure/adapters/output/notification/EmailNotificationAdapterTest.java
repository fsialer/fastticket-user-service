package com.fernando.fastticket_user_service.infrastructure.adapters.output.notification;

import com.fernando.fastticket_user_service.domain.models.User;
import com.fernando.fastticket_user_service.utils.TestUtilUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailNotificationAdapterTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailNotificationAdapter emailNotificationAdapter;

    @Test
    @DisplayName("When Send Email Successfully Expect No Exception")
    void When_SendEmailSuccessfully_Expect_NoException() {
        // Arrange
        User user = TestUtilUser.mockUser();
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        
        // Act
        emailNotificationAdapter.send(user);
        
        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("When Send Email Throws RuntimeException Expect Exception Handled")
    void When_SendEmailThrowsRuntimeException_Expect_ExceptionHandled() {
        // Arrange
        User user = TestUtilUser.mockUser();
        doThrow(new RuntimeException("SMTP connection failed")).when(mailSender).send(any(SimpleMailMessage.class));
        
        // Act & Assert (no exception should be thrown)
        emailNotificationAdapter.send(user);
        
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}