package com.survey.surveysystem.web.dto.question.response;

import com.survey.surveysystem.domain.entity.Question;

import java.util.List;

public record QuestionDetailResponse(
        Long id,
        int questionNum,
        String content,
        Question.QuestionType type,
        boolean isRequired,
        boolean isSatisfaction,
        List<QuestionOptionResponse> options
) {
    public static QuestionDetailResponse from(Question question, int questionNum, List<QuestionOptionResponse> options) {
        return new QuestionDetailResponse(
                question.getId(),
                questionNum,
                question.getContent(),
                question.getType(),
                question.isRequired(),
                question.isSatisfaction(),
                options
        );
    }
}
