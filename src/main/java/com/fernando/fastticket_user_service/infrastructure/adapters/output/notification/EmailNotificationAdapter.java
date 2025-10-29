package com.fernando.fastticket_user_service.infrastructure.adapters.output.notification;

import com.fernando.fastticket_user_service.application.ports.output.NotificationPort;
import com.fernando.fastticket_user_service.domain.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationAdapter implements NotificationPort {
    private final JavaMailSender mailSender;
    @Override
    public void send(User user) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("FASTTICKET: Verificación de correo");
            message.setText("Hola, este correo se envió para poder verificar su registro.");
            mailSender.send(message);
            log.debug("Email sent successfully to: {}", user.getEmail());
        } catch (RuntimeException e) {
            log.error("Failed to send email to: {}. Error: {}", user.getEmail(), e.getMessage());
        }
    }
}
