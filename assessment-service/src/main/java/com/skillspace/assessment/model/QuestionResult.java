package com.skillspace.assessment.model;
import lombok.Data;

@Data
public class QuestionResult {
    private String question_text;
    private String correct_answer;
    private String selectedAnswer;
    private boolean correct;
    private String imageUrl;
}
