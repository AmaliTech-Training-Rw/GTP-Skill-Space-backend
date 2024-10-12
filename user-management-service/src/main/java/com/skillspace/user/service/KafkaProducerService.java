package com.skillspace.user.service;

import com.skillspace.user.dto.CompanyStatusChangeEvent;
import com.skillspace.user.dto.TalentAccountCreatedEvent;
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
    public KafkaTemplate<String, CompanyStatusChangeEvent> companyTemplate;

    // helper method to handle Kafka event sending logic
    public void sendTalentActivationCodeEvent(Talent registeredTalent, String email, String activationCode) {
        TalentAccountCreatedEvent event = new TalentAccountCreatedEvent();
        event.setFirstName(registeredTalent.getFirstName());
        event.setLastName(registeredTalent.getLastName());
        event.setEmail(email);
        event.setActivationCode(activationCode);
        talentTemplate.send("talent-accounts", event);
    }

    public void sendCompanyApprovalStatusEvent(Company company, String status) {
        CompanyStatusChangeEvent event = new CompanyStatusChangeEvent();
        event.setCompanyId(company.getCompanyId());
        event.setCompanyName(company.getName());
        event.setEmail(company.getUserId().getEmail());
        event.setStatus(status);
        companyTemplate.send("company-approval-status", event);
    }
}
