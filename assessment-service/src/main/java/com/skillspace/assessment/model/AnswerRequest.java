package com.skillspace.assessment.model;
import lombok.Data;
import java.util.UUID;

@Data
public class AnswerRequest {
    private UUID questionId;
    private String selectedAnswer;
}

