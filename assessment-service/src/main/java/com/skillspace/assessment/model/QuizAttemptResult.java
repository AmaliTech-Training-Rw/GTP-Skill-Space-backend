package com.skillspace.assessment.model;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuizAttemptResult {
    private int score;
    private int totalScore;
    private boolean passed;
    private String badge;
    private List<QuestionResult> questionResults = new ArrayList<>();
}

