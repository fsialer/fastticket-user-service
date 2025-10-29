package com.fernando.fastticket_user_service.infrastructure.config;

import com.fernando.fastticket_user_service.infrastructure.adapters.output.notification.facade.NotificationFacade;
import com.fernando.fastticket_user_service.infrastructure.adapters.output.notification.EmailNotificationAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;

@Configuration
public class NotificationConfig {

    @Bean
    public EmailNotificationAdapter emailNotificationAdapter(JavaMailSender mailSender){
        return new EmailNotificationAdapter(mailSender);
    }

    @Bean
    public NotificationFacade notificationFacade(EmailNotificationAdapter emailAdapter) {
        return new NotificationFacade(List.of(emailAdapter));
    }
}
