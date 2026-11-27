package com.survey.surveysystem.web.controller;

import com.survey.surveysystem.domain.entity.Question;
import com.survey.surveysystem.domain.service.QuestionService;
import com.survey.surveysystem.web.dto.question.request.QuestionCreateRequest;
import com.survey.surveysystem.web.dto.question.request.QuestionUpdateRequest;
import com.survey.surveysystem.web.dto.question.response.QuestionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.IntStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/surveys/{surveyId}/questions")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<QuestionResponse> createQuestion(@PathVariable Long surveyId, @Valid @RequestBody QuestionCreateRequest request) {
        Question question = questionService.createQuestion(
                surveyId, request.content(), request.type(), request.isRequired(), request.isSatisfaction()
        );
        List<Question> questions = questionService.getQuestions(surveyId);
        int questionNumber = questions.indexOf(question) + 1;
        return ResponseEntity.status(HttpStatus.CREATED).body(QuestionResponse.from(question, questionNumber));
    }

    @GetMapping
    public ResponseEntity<List<QuestionResponse>> getQuestions(@PathVariable Long surveyId) {
        List<Question> questions = questionService.getQuestions(surveyId);
        List<QuestionResponse> list = IntStream.range(0, questions.size())
                .mapToObj(i -> QuestionResponse.from(questions.get(i), i + 1))
                .toList();

        return ResponseEntity.ok(list);
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<Void> updateQuestion(@PathVariable Long surveyId, @PathVariable Long questionId,
            @Valid @RequestBody QuestionUpdateRequest request) {
        questionService.updateQuestion(questionId, request.content(), request.isRequired());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long surveyId, @PathVariable Long questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }
}
