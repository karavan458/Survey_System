package com.survey.surveysystem.web.dto.user.request;

import jakarta.validation.constraints.NotNull;

public record UserPositionChangeRequest(
        @NotNull
        String position
) {
}
