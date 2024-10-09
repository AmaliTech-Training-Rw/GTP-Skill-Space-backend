package com.skillspace.assessment.service;

import com.skillspace.assessment.model.Question;
import com.skillspace.assessment.model.Quiz;
import com.skillspace.assessment.model.QuizAttempt;
import com.skillspace.assessment.repositories.QuizAttemptRepository;
import com.skillspace.assessment.repositories.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final UserService userService;

    public Quiz createQuiz(Quiz quiz) {
        quiz.setId(UUID.randomUUID());
        quiz.setCreatedAt(LocalDateTime.now());
        quiz.setUpdatedAt(LocalDateTime.now());
        quiz.setTotalPoints(calculateTotalPoints(quiz));  // Calculate total points
        return quizRepository.save(quiz);
    }

    public Quiz getQuizById(UUID id) {
        return quizRepository.findById(id).orElseThrow(() -> new RuntimeException("Quiz not found"));
    }

    public Quiz updateQuiz(UUID id, Quiz quizDetails) {
        Quiz existingQuiz = getQuizById(id);
        existingQuiz.setTitle(quizDetails.getTitle());
        existingQuiz.setQuestions(quizDetails.getQuestions());
        existingQuiz.setTimeLimit(quizDetails.getTimeLimit());
        existingQuiz.setPassingScore(quizDetails.getPassingScore());
        existingQuiz.setUpdatedAt(LocalDateTime.now());
        return quizRepository.save(existingQuiz);
    }

    public void deleteQuiz(UUID id) {
        quizRepository.deleteById(id);
    }
    public int calculateScore(UUID quizId, List<String> userAnswers) {
        Quiz quiz = getQuizById(quizId);
        int score = 0;
        List<Question> questions = quiz.getQuestions();

        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getCorrect_answer().equals(userAnswers.get(i))) {
                score += questions.get(i).getPoints();
            }
        }

        return score;
    }

    public void saveQuizAttempt(UUID userId, UUID quizId, List<String> userAnswers) {
        int score = calculateScore(quizId, userAnswers);
        QuizAttempt attempt = new QuizAttempt();
        attempt.setAttemptId(UUID.randomUUID().toString());
        attempt.setUserId(userId.toString());
        attempt.setQuizId(quizId.toString());
        attempt.setUserAnswers(userAnswers);
        attempt.setScore(score);
        attempt.setTimestamp(System.currentTimeMillis());

        quizAttemptRepository.save(attempt); // Save the quiz attempt

        // Optionally award a badge based on score
//        if (score >= quiz.getPassingScore()) {
//            userService.awardBadge(userId, /* badgeId for completion */);
//        }
    }

    public List<Quiz> filterQuizzes(String companyId, String title, String programId) {
        return quizRepository.findAll();
    }

    public boolean evaluateQuizCompletion(UUID quizId, int score) {
        Quiz quiz = getQuizById(quizId);
        return score >= quiz.getPassingScore();
    }

    private int calculateTotalPoints(Quiz quiz) {
        return quiz.getQuestions().stream().mapToInt(q -> q.getPoints()).sum();
    }

    public boolean canRetryQuiz(UUID quizId) {
        Quiz quiz = getQuizById(quizId);
        LocalDateTime lastAttempt = quiz.getUpdatedAt();
        return LocalDateTime.now().isAfter(lastAttempt.plusDays(7));
    }
}
