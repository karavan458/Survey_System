package com.survey.surveysystem.web.controller;

import com.survey.surveysystem.domain.entity.Survey;
import com.survey.surveysystem.domain.service.SurveyService;
import com.survey.surveysystem.web.dto.survey.request.SurveyCreateRequest;
import com.survey.surveysystem.web.dto.survey.request.SurveyStatusChangeRequest;
import com.survey.surveysystem.web.dto.survey.request.SurveyUpdateRequest;
import com.survey.surveysystem.web.dto.survey.response.SurveyResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/surveys")
public class SurveyController {

    private final SurveyService surveyService;

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
