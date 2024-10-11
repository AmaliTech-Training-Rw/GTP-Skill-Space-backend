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

    @KafkaListener(topics = "talent-accounts", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(TalentAccountCreatedEvent event) {
        try {
            // Extract email directly from the event object
            String recipientEmail = event.getEmail();
            String firstName = event.getFirstName();
            String lastName = event.getLastName();
            sendNotification(recipientEmail, firstName, lastName);
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }

    private void sendNotification(String email, String firstName, String lastName) {
        // Notification logic
        String subject = "Welcome, " + firstName + "!";
        String message = "Dear " + firstName + " " + lastName + ", your account has been created.";
        notificationService.sendEmail(email, subject, message);
    }
}
