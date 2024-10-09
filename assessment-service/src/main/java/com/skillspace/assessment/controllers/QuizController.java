package com.skillspace.assessment.controllers;

import com.skillspace.assessment.model.Quiz;
import com.skillspace.assessment.model.QuizAttempt;
import com.skillspace.assessment.model.QuizAttemptRequest;
import com.skillspace.assessment.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@Valid @RequestBody Quiz quiz) {
        if (quiz.getQuestions() == null || quiz.getQuestions().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        Quiz createdQuiz = quizService.createQuiz(quiz);
        return new ResponseEntity<>(createdQuiz, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable UUID id) {
        Quiz quiz = quizService.getQuizById(id);
        return ResponseEntity.ok(quiz);
    }
    @PostMapping("/{id}/attempt")
    public ResponseEntity<String> attemptQuiz(@PathVariable UUID id, @Valid @RequestBody QuizAttemptRequest request) {
        // You might want to create a default user ID or just log this attempt
        UUID dummyUserId = UUID.randomUUID(); // Placeholder for user ID since you don't have a user
        quizService.saveQuizAttempt(dummyUserId, id, request.getUserAnswers());
        return ResponseEntity.ok("Quiz attempt recorded.");
    }



    // Get All Quizzes (with filtering)
    @GetMapping
    public ResponseEntity<List<Quiz>> getAllQuizzes(
            @RequestParam(required = false) String companyId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String programId) {
        List<Quiz> quizzes = quizService.filterQuizzes(companyId, title, programId);
        return ResponseEntity.ok(quizzes);
    }

    // Update Quiz
    @PutMapping("/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable UUID id, @RequestBody Quiz quiz) {
        Quiz updatedQuiz = quizService.updateQuiz(id, quiz);
        return ResponseEntity.ok(updatedQuiz);
    }

    // Delete Quiz
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable UUID id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }

    // Complete Quiz (after an attempt)
    @PostMapping("/{id}/complete")
    public ResponseEntity<String> completeQuiz(@PathVariable UUID id, @RequestParam int score) {
        boolean passed = quizService.evaluateQuizCompletion(id, score);
        if (passed) {
            // Award badge and add to profile
            return ResponseEntity.ok("Quiz passed, badge awarded!");
        } else {
            return ResponseEntity.ok("Quiz failed, try again in a week.");
        }
    }

    // Retry quiz after a week (blocked for 7 days)
    @PostMapping("/{id}/retry")
    public ResponseEntity<String> retryQuiz(@PathVariable UUID id) {
        boolean retryAllowed = quizService.canRetryQuiz(id);
        if (retryAllowed) {
            return ResponseEntity.ok("You can retake the quiz.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You must wait 7 days to retry.");
        }
    }
}
