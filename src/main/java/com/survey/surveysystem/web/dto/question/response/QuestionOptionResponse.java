package com.survey.surveysystem.web.dto.question.response;

import com.survey.surveysystem.domain.entity.QuestionOption;

public record QuestionOptionResponse(
        Long id,
        Long questionId,
        int optionNumber,
        String content
) {
    public static QuestionOptionResponse from(QuestionOption option, int optionNumber) {
        return new QuestionOptionResponse(
                option.getId(),
                option.getQuestion().getId(),
                optionNumber,
                option.getContent()
        );
    }
}
