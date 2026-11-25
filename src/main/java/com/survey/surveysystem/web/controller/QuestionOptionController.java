package com.survey.surveysystem.web.controller;

import com.survey.surveysystem.domain.entity.QuestionOption;
import com.survey.surveysystem.domain.service.QuestionOptionService;
import com.survey.surveysystem.web.dto.question.request.QuestionOptionCreateRequest;
import com.survey.surveysystem.web.dto.question.request.QuestionOptionUpdateRequest;
import com.survey.surveysystem.web.dto.question.response.QuestionOptionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.IntStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions/{questionId}/options")
public class QuestionOptionController {

    private final QuestionOptionService optionService;

    @PostMapping
    public ResponseEntity<QuestionOptionResponse> createOption(
            @PathVariable Long questionId, @Valid @RequestBody QuestionOptionCreateRequest request) {
        QuestionOption option = optionService.createOption(questionId, request.content());
        List<QuestionOption> options = optionService.getOptions(questionId);
        int optionNumber = options.indexOf(option) + 1;
        return ResponseEntity.status(HttpStatus.CREATED).body(QuestionOptionResponse.from(option, optionNumber));
    }

    @GetMapping
    public ResponseEntity<List<QuestionOptionResponse>> getOptions(@PathVariable Long questionId) {
        List<QuestionOption> options = optionService.getOptions(questionId);
        List<QuestionOptionResponse> reuslt = IntStream.range(0, options.size())
                .mapToObj(i -> QuestionOptionResponse.from(options.get(i), i + 1))
                .toList();
        return ResponseEntity.ok(reuslt);
    }

    @PutMapping("/{optionId}")
    public ResponseEntity<Void> updateOption(@PathVariable Long questionId, @PathVariable Long optionId,
            @Valid @RequestBody QuestionOptionUpdateRequest request) {
        optionService.updateOption(optionId, request.content());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long questionId, @PathVariable Long optionId) {
        optionService.deleteOption(optionId);
        return ResponseEntity.noContent().build();
    }
}
