package com.survey.surveysystem.web.dto.survey.response;

import com.survey.surveysystem.domain.entity.SurveyTarget;

public record SurveyTargetResponse(
        Long departmentId,
        String departmentName
) {
    public static SurveyTargetResponse from(SurveyTarget target) {
        return new SurveyTargetResponse(
                target.getDepartment().getId(),
                target.getDepartment().getName()
        );
    }
}
