package com.skillspace.notification.consumer;

import com.skillspace.notification.dto.PasswordChangedEvent;
import com.skillspace.notification.dto.PasswordResetEvent;
import com.skillspace.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetConsumer {

    @Autowired
    private NotificationService notificationService;

    // method to listen for password reset events and send  email with OTP
    @KafkaListener(topics = "password-reset-otp", groupId = "notification-group")
    public void consumePasswordResetEvent(PasswordResetEvent event) {
        notificationService.sendPasswordResetOTPEmail(event.getEmail(), event.getOtp());
    }

    // method to listen for password changed events and send corresponding email
    @KafkaListener(topics = "password-changed", groupId = "notification-group")
    public void consumePasswordChangedEvent(PasswordChangedEvent event) {
        notificationService.sendPasswordChangedEmail(event.getEmail());
    }
}
