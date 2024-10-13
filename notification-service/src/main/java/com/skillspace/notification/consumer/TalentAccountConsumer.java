package com.skillspace.notification.consumer;

import com.skillspace.notification.dto.TalentAccountCreatedEvent;
import com.skillspace.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@EnableKafka
public class TalentAccountConsumer {

    @Autowired
    private NotificationService notificationService;

    // method to listen for talent account creation and send corresponding mail with activationCode
    @KafkaListener(topics = "talent-accounts-activation", groupId = "notification-group")
    public void listen(TalentAccountCreatedEvent event) {
            String recipientEmail = event.getEmail();
            String firstName = event.getFirstName();
            String lastName = event.getLastName();
            String activationCode = event.getActivationCode();
            notificationService.sendTalentActivationEmail(recipientEmail, firstName, lastName, activationCode);
    }

}