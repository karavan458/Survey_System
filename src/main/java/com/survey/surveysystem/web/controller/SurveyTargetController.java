package com.survey.surveysystem.web.controller;

import com.survey.surveysystem.domain.entity.SurveyTarget;
import com.survey.surveysystem.domain.service.SurveyTargetService;
import com.survey.surveysystem.web.dto.survey.request.SurveyTargetSetRequest;
import com.survey.surveysystem.web.dto.survey.response.SurveyTargetResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/surveys/{surveyId}/targets")
public class SurveyTargetController {

    private final SurveyTargetService targetService;

    @PostMapping
    public ResponseEntity<Void> setTargets(@PathVariable Long surveyId, @Valid @RequestBody SurveyTargetSetRequest request) {
        targetService.setTargets(surveyId, request.departmentIds());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SurveyTargetResponse>> getTargets(@PathVariable Long surveyId) {
        List<SurveyTarget> targets = targetService.getTargets(surveyId);
        List<SurveyTargetResponse> list = targets.stream()
                .map(SurveyTargetResponse::from)
                .toList();

        return ResponseEntity.ok(list);
    }
}
