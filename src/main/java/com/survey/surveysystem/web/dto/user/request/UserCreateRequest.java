package com.survey.surveysystem.web.dto.user.request;

import com.survey.surveysystem.domain.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserCreateRequest(
        @Email @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String name,
        @NotNull
        User.Role role,
        @NotNull
        Long departmentId,
        String position,
        @NotNull
        LocalDate hireDate
) {
}
