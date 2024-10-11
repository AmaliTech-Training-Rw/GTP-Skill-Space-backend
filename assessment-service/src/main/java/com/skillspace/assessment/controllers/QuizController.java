package com.skillspace.assessment.controllers;

import com.skillspace.assessment.model.Quiz;
import com.skillspace.assessment.model.QuizAttemptRequest;
import com.skillspace.assessment.model.QuizAttemptResult;
import com.skillspace.assessment.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/assessments/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;


    @PostMapping
    public ResponseEntity<Quiz> createQuiz(@Valid @RequestPart Quiz quiz, @RequestPart MultipartFile image) {
        if (quiz.getQuestions() == null || quiz.getQuestions().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        Quiz createdQuiz;
        try {
            createdQuiz = quizService.createQuiz(quiz, image);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return new ResponseEntity<>(createdQuiz, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable UUID id) {
        Quiz quiz = quizService.getQuizById(id);
        return ResponseEntity.ok(quiz);
    }

    @GetMapping
    public ResponseEntity<List<Quiz>> getAllQuizzes(
            @RequestParam(required = false) String companyId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String programId) {
        List<Quiz> quizzes = quizService.filterQuizzes(companyId, title, programId);
        return ResponseEntity.ok(quizzes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable UUID id, @RequestBody Quiz quiz) {
        Quiz updatedQuiz = quizService.updateQuiz(id, quiz);
        return ResponseEntity.ok(updatedQuiz);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable UUID id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/attempt")
    public ResponseEntity<QuizAttemptResult> attemptQuiz(@PathVariable UUID id, @Valid @RequestBody QuizAttemptRequest request) {
        UUID dummyUserId = UUID.randomUUID();
        List<String> userAnswers = new ArrayList<>();
        userAnswers.add(request.getAnswer1());
        userAnswers.add(request.getAnswer2());
        userAnswers.add(request.getAnswer3());
        userAnswers.add(request.getAnswer4());
        userAnswers.add(request.getAnswer5());
        userAnswers.removeIf(answer -> answer == null || answer.isEmpty());

        QuizAttemptResult result = quizService.saveQuizAttempt(dummyUserId, id, userAnswers);
        return ResponseEntity.ok(result);
    }

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
