package com.skillspace.assessment.model;


import lombok.Data;
import java.util.List;

@Data
public class QuizAttemptRequest {
    private List<String> userAnswers; // User's answers for the quiz
}


