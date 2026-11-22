package com.survey.surveysystem.web.dto.user.request;

import com.survey.surveysystem.domain.entity.User;
import jakarta.validation.constraints.NotNull;

public record UserRoleChangeRequest(
        @NotNull
        User.Role role
) {
}
