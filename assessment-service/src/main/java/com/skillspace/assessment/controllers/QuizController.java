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
    public ResponseEntity<Quiz> createQuiz(@Valid @RequestPart Quiz quiz, @RequestPart(required = false) MultipartFile image) {
        if (quiz.getQuestions() == null || quiz.getQuestions().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            Quiz createdQuiz = quizService.createQuiz(quiz, image);
            return new ResponseEntity<>(createdQuiz, HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable UUID id) {
        try {
            Quiz quiz = quizService.getQuizById(id);
            return ResponseEntity.ok(quiz);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Quiz>> getAllQuizzes(
            @RequestParam(required = false) String companyId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String programId) {
        List<Quiz> quizzes = quizService.filterQuizzes(companyId, title, programId);
        return ResponseEntity.ok(quizzes);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Quiz> updateQuiz(
//            @PathVariable UUID id,
//            @Valid @RequestPart Quiz quizDetails,
//            @RequestPart(required = false) MultipartFile image) {
//        try {
//            Quiz updatedQuiz = quizService.updateQuiz(id, quizDetails, image);
//            return ResponseEntity.ok(updatedQuiz);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }
@PutMapping("/{id}")
public ResponseEntity<Quiz> updateQuiz(
        @PathVariable UUID id,
        @RequestBody Quiz updatedQuiz) {

    // Call the service method to update the quiz
    Quiz quiz = quizService.updateQuiz(id, updatedQuiz);

    return ResponseEntity.ok(quiz);
}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable UUID id) {
        try {
            quizService.deleteQuiz(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
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

        try {
            QuizAttemptResult result = quizService.saveQuizAttempt(dummyUserId, id, userAnswers);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Quiz>> getQuizzesByCompanyId(@PathVariable String companyId) {
        List<Quiz> quizzes = quizService.getQuizzesByCompanyId(companyId);
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/global")
    public ResponseEntity<List<Quiz>> getGlobalQuizzes() {
        List<Quiz> quizzes = quizService.getGlobalQuizzes();
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/local/{companyId}")
    public ResponseEntity<List<Quiz>> getLocalQuizzes(@PathVariable String companyId) {
        List<Quiz> quizzes = quizService.getLocalQuizzes(companyId);
        return ResponseEntity.ok(quizzes);
    }
    @GetMapping("/title/{title}")
    public ResponseEntity<List<Quiz>> getQuizzesByTitle(@PathVariable String title) {
        List<Quiz> quizzes = quizService.getQuizzesByTitle(title);
        return ResponseEntity.ok(quizzes);
    }
    @GetMapping("/filter")
    public ResponseEntity<List<Quiz>> filterQuizzes(
            @RequestParam(required = false) String companyId,
            @RequestParam(required = false) String title) {
        List<Quiz> quizzes = quizService.filterQuizzes(companyId, title);
        return ResponseEntity.ok(quizzes);
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
