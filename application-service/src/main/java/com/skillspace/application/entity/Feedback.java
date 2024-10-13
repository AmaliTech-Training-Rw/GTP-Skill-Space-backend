package com.skillspace.application.entity;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;

import java.util.UUID;

@Data
public class Feedback {
    @PrimaryKey
    private UUID id;

    private UUID applicationId;
    private String feedbackMessage;
    private String replyMessage;
}
