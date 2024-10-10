package com.skillspace.notification.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @KafkaListener(topics = "account-created", groupId = "skillspace-notifications-messenger")
    public void handleAccountCreatedEvent(String email) {
        // Logic to send OTP, approval emails, etc.
        sendOtp(email);
        sendAccountApprovalRequest(email);
    }

    private void sendOtp(String email) {
        // Send OTP logic
    }

    private void sendAccountApprovalRequest(String email) {
        // Send approval email to System Administrator
    }
}


