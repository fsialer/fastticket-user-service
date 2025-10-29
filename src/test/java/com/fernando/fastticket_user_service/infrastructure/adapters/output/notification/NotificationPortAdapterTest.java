package com.fernando.fastticket_user_service.infrastructure.adapters.output.notification;

import com.fernando.fastticket_user_service.domain.models.User;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.notification.facade.NotificationFacade;
import com.fernando.fastticket_user_service.utils.TestUtilUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationPortAdapterTest {

    private NotificationFacade notificationFacade;

    @Mock
    private  JavaMailSender mailSender;

    @BeforeEach
    void setup() {
        // Crea el adaptador real o mock que use el mailSender
        var emailNotificationAdapter = new EmailNotificationAdapter(mailSender);

        // Inyecta una lista con ese adaptador en el Facade
        notificationFacade = new NotificationFacade(List.of(emailNotificationAdapter));
    }

    @Test
    @DisplayName("When User Registered Correctly Expect Send Email Confirmation")
    void When_UserRegisteredCorrectly_Expect_SendEmailConfirmation() {
        // Arrange
        User user = TestUtilUser.mockUser();
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        
        // Act
        notificationFacade.notifyUser(user);
        
        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Expect Exception When Send Email Fail")
    void Expect_Exception_When_SendEmailFail() {
        // Arrange
        User user = TestUtilUser.mockUser();
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Act
        notificationFacade.notifyUser(user);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

}
