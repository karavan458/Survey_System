package com.survey.surveysystem.web.dto.user.response;

import com.survey.surveysystem.domain.entity.User;

import java.time.LocalDate;

public record UserResponse(
        Long id,
        String email,
        String name,
        User.Role role,
        Long departmentId,
        String departmentName,
        String position,
        LocalDate hireDate
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getDepartment().getId(),
                user.getDepartment().getName(),
                user.getPosition(),
                user.getHireDate()
        );
    }
}
