package com.skillspace.notification.consumer;

import com.skillspace.notification.dto.CompanyAccountCreatedEvent;
import com.skillspace.notification.dto.CompanyStatusChangeEvent;
import com.skillspace.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CompanyApprovalsConsumer {
    @Autowired
    private NotificationService notificationService;

    // method to listen for company status change events and send corresponding response email
    @KafkaListener(topics = "company-approval-status", groupId = "notification-group")
    public void consumeCompanyStatusChangeEvent(CompanyStatusChangeEvent event) {
        if ("APPROVED".equals(event.getStatus())) {
            notificationService.sendCompanyApprovalEmail(event.getEmail(), event.getCompanyName());
        } else if ("REJECTED".equals(event.getStatus())) {
            notificationService.sendCompanyRejectionEmail(event.getEmail(), event.getCompanyName());
        }
    }

    // method to listen for company account verification events and send corresponding email
    @KafkaListener(topics = "company-accounts-verification", groupId = "notification-group")
    public void listen(CompanyAccountCreatedEvent event) {
        String recipientEmail = event.getCompanyEmail();
        String companyName = event.getCompanyName();
        String activationCode = event.getActivationCode();
        notificationService.sendCompanyVerificationEmail(recipientEmail, companyName, activationCode);
    }
}
