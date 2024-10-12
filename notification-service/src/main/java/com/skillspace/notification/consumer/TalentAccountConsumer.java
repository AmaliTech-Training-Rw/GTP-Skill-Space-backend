package com.skillspace.notification.consumer;

import com.skillspace.notification.dto.TalentAccountCreatedEvent;
import com.skillspace.notification.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;


@Service
@EnableKafka
public class TalentAccountConsumer {

    @Autowired
    private NotificationService notificationService;

    private static final Logger log = LoggerFactory.getLogger(TalentAccountConsumer.class);

    @KafkaListener(topics = "talent-accounts", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(TalentAccountCreatedEvent event,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                       @Header(KafkaHeaders.OFFSET) long offset) {
        try {
            String recipientEmail = event.getEmail();
            String firstName = event.getFirstName();
            String lastName = event.getLastName();
            String activationCode = event.getActivationCode();

            sendNotification(recipientEmail, firstName, lastName, activationCode);

            log.info("Successfully processed message: topic = {}, partition = {}, offset = {}, event = {}",
                    topic, partition, offset, event);
        } catch (Exception e) {
            log.error("Error processing message: " + e.getMessage(), e);
        }
    }

    private void sendNotification(String email, String firstName, String lastName, String activationCode) {
        String subject = "Welcome, " + firstName + "! Activate Your Account";
        String message = String.format(
                "Dear %s %s,\n\n" +
                        "Your account has been created. Please use the following code to activate your account:\n\n" +
                        "%s\n\n" +
                        "If you did not create this account, please ignore this email.",
                firstName, lastName, activationCode
        );
        notificationService.sendEmail(email, subject, message);
    }
}