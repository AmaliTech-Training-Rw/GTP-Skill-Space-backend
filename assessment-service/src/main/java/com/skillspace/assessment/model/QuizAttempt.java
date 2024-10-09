package com.skillspace.assessment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("quiz_attempts")
public class QuizAttempt {
    @PrimaryKey
    private String attemptId;

    private String userId;
    private String quizId;
    private List<String> userAnswers;
    private int score;
    private long timestamp;

    // Additional fields as needed
}
