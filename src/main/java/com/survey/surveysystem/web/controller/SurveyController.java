package com.survey.surveysystem.web.controller;

import com.survey.surveysystem.domain.entity.Question;
import com.survey.surveysystem.domain.entity.QuestionOption;
import com.survey.surveysystem.domain.entity.Survey;
import com.survey.surveysystem.domain.service.QuestionOptionService;
import com.survey.surveysystem.domain.service.QuestionService;
import com.survey.surveysystem.domain.service.SurveyService;
import com.survey.surveysystem.web.dto.question.response.QuestionDetailResponse;
import com.survey.surveysystem.web.dto.question.response.QuestionOptionResponse;
import com.survey.surveysystem.web.dto.survey.request.SurveyCreateRequest;
import com.survey.surveysystem.web.dto.survey.request.SurveyStatusChangeRequest;
import com.survey.surveysystem.web.dto.survey.request.SurveyUpdateRequest;
import com.survey.surveysystem.web.dto.survey.response.SurveyDetailResponse;
import com.survey.surveysystem.web.dto.survey.response.SurveyResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/surveys")
public class SurveyController {

    private final SurveyService surveyService;
    private final QuestionService questionService;
    private final QuestionOptionService optionService;

    @PostMapping
    public ResponseEntity<SurveyResponse> createSurvey(@Valid @RequestBody SurveyCreateRequest request) {
        Survey survey = surveyService.createSurvey(
                request.title(), request.description(), request.isAnonymous(),
                request.startDate(), request.endDate(), request.creatorId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(SurveyResponse.from(survey));
    }

    @GetMapping
    public ResponseEntity<List<SurveyResponse>> getSurveys() {
        List<SurveyResponse> list = surveyService.getSurveys().stream()
                .map(SurveyResponse::from)
                .toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SurveyDetailResponse> getSurvey(@PathVariable Long id) {
        Survey survey = surveyService.getSurvey(id);
        List<Question> questions = questionService.getQuestions(id);

        List<Long> questionIds = questions.stream()
                .map(Question::getId)
                .toList();
        Map<Long, List<QuestionOption>> optionLookup = optionService.getOptionLookup(questionIds);

        List<QuestionDetailResponse> questionResponses = IntStream.range(0, questions.size())
                .mapToObj(i -> {
                    Question question = questions.get(i);
                    List<QuestionOption> options = optionLookup.getOrDefault(question.getId(), List.of());
                    List<QuestionOptionResponse> optionResponses = IntStream.range(0, options.size())
                            .mapToObj(j -> QuestionOptionResponse.from(options.get(j), j + 1))
                            .toList();
                    return QuestionDetailResponse.from(question, i + 1, optionResponses);
                })
                .toList();

        return ResponseEntity.ok(SurveyDetailResponse.from(survey, questionResponses));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSurvey(@PathVariable Long id, @Valid @RequestBody SurveyUpdateRequest request) {
        surveyService.updateSurvey(id, request.title(), request.description(), request.startDate(), request.endDate());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable Long id, @Valid @RequestBody SurveyStatusChangeRequest request) {
        surveyService.changeStatus(id, request.status());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
        surveyService.deleteSurvey(id);
        return ResponseEntity.noContent().build();
    }
}
