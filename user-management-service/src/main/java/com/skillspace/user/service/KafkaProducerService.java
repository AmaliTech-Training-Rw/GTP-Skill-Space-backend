package com.skillspace.user.service;

import com.skillspace.user.dto.*;
import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.Company;
import com.skillspace.user.entity.Talent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    public KafkaTemplate<String, TalentAccountCreatedEvent> talentTemplate;

    @Autowired
    public KafkaTemplate<String, CompanyAccountCreatedEvent> companyCreatedTemplate;

    @Autowired
    public KafkaTemplate<String, CompanyStatusChangeEvent> companyTemplate;

    @Autowired
    public KafkaTemplate<String, PasswordResetEvent> passwordResetTemplate;

    @Autowired
    public KafkaTemplate<String, PasswordChangedEvent> passwordChangedTemplate;

    // Method to handle sending company activation code event
    public void sendCompanyVerificationCodeEvent(Company registeredCompany, String email, String activationCode) {
        CompanyAccountCreatedEvent event = new CompanyAccountCreatedEvent();
        event.setCompanyName(registeredCompany.getName());
        event.setCompanyEmail(email);
        event.setActivationCode(activationCode);
        companyCreatedTemplate.send("company-accounts-verification", event);
    }

    // helper method to handle sending talent account activation code event
    public void sendTalentActivationCodeEvent(Talent registeredTalent, String email, String activationCode) {
        TalentAccountCreatedEvent event = new TalentAccountCreatedEvent();
        event.setFirstName(registeredTalent.getFirstName());
        event.setLastName(registeredTalent.getLastName());
        event.setEmail(email);
        event.setActivationCode(activationCode);
        talentTemplate.send("talent-accounts-activation", event);
    }

    // method to handle sending company approval event
    public void sendCompanyApprovalStatusEvent(Company company, String status) {
        CompanyStatusChangeEvent event = new CompanyStatusChangeEvent();
        event.setCompanyId(company.getCompanyId());
        event.setCompanyName(company.getName());
        event.setEmail(company.getUserId().getEmail());
        event.setStatus(status);
        companyTemplate.send("company-approval-status", event);
    }

    // method to handle sending of OTP
    public void sendPasswordResetOTPEvent(String email, String otp) {
        PasswordResetEvent event = new PasswordResetEvent();
        event.setEmail(email);
        event.setOtp(otp);
        passwordResetTemplate.send("password-reset-otp", event);
    }

    // method to send password changed event
    public void sendPasswordChangedEvent(Account account) {
        PasswordChangedEvent event = new PasswordChangedEvent();
        event.setEmail(account.getEmail());
        passwordChangedTemplate.send("password-changed", event);
    }
}
