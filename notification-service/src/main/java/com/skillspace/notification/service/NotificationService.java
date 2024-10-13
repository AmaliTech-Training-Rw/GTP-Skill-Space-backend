package com.skillspace.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    // helper method template to handle email structure
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    // email template for company verification email
    public void sendCompanyVerificationEmail(String email, String companyName, String activationCode) {
        String subject = "Activate Your Company Account: " + companyName;
        String message = String.format(
                "Dear %s,\n\n" +
                        "Your company account has been successfully created. Please use the following code to verify your company account:\n\n" +
                        "%s\n\n" +
                        "Once verified, an administrator will review your account for approval.\n\n" +
                        "If you did not create this account, please ignore this email.",
                companyName, activationCode
        );
        sendEmail(email, subject, message);
    }


    // email template for talent verification email
    public void sendTalentActivationEmail(String email, String firstName, String lastName, String activationCode) {
        String subject = "Welcome, " + firstName + "! Activate Your Account";
        String message = String.format(
                "Dear %s %s,\n\n" +
                        "Your account has been created. Please use the following code to activate your account:\n\n" +
                        "%s\n\n" +
                        "If you did not create this account, please ignore this email.",
                firstName, lastName, activationCode
        );
        sendEmail(email, subject, message);
    }

    // email template for notifying company about successful account approval
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

    // email template for notifying company about account rejection
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

    // email template for sending out password reset OTP
    public void sendPasswordResetOTPEmail(String email, String otp) {
        String subject = "Password Reset OTP";
        String message = String.format(
                "Your password reset OTP is: %s\n\n" +
                        "This OTP will expire in 10 minutes. If you did not request a password reset, please ignore this email.",
                otp
        );
        sendEmail(email, subject, message);
    }

    // email template for alerting users about password change
    public void sendPasswordChangedEmail(String email) {
        String subject = "Password Changed Successfully";
        String message = "Your password has been successfully changed. If you did not make this change, please contact support immediately.";
        sendEmail(email, subject, message);
    }

}

