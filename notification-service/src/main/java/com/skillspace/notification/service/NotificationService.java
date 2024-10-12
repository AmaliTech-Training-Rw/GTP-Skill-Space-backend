package com.skillspace.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendCompanyApprovalEmail(String email, String companyName) {
        String subject = "SkillSpace Company Account Update";
        String body = String.format(
                "Dear %s,\n\n" +
                        "We are pleased to inform you that your company account has been approved.\n" +
                        "You can now log in and start using our platform.\n\n" +
                        "Best regards,\nThe Admin Team",
                companyName
        );
        sendEmail(email, subject, body);
    }

    public void sendCompanyRejectionEmail(String email, String companyName) {
        String subject = "SkillSpace Company Account Update";
        String body = String.format(
                "Dear %s,\n\n" +
                        "We regret to inform you that your company account application has been rejected.\n" +
                        "If you have any questions, please contact our support team.\n\n" +
                        "Best regards,\nThe Admin Team",
                companyName
        );
        sendEmail(email, subject, body);
    }

}

