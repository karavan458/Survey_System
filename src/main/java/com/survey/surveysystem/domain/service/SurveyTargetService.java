package com.survey.surveysystem.domain.service;

import com.survey.surveysystem.domain.entity.Department;
import com.survey.surveysystem.domain.entity.Survey;
import com.survey.surveysystem.domain.entity.SurveyTarget;
import com.survey.surveysystem.domain.entity.User;
import com.survey.surveysystem.domain.repository.department.DepartmentRepository;
import com.survey.surveysystem.domain.repository.survey.SurveyRepository;
import com.survey.surveysystem.domain.repository.survey.SurveyTargetRepository;
import com.survey.surveysystem.global.exception.CustomException;
import com.survey.surveysystem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyTargetService {

    private final SurveyTargetRepository targetRepository;
    private final SurveyRepository surveyRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public void setTargets(Long surveyId, List<Long> departmentIds) {
        Survey survey = findSurvey(surveyId);
        validateDraft(survey);
        validateManagerScope(survey, departmentIds);

        targetRepository.deleteBySurveyId(surveyId);

        List<SurveyTarget> targets = departmentIds.stream()
                .map(this::findDepartment)
                .map(department -> SurveyTarget.create(survey, department))
                .toList();

        targetRepository.saveAll(targets);
    }

    public List<SurveyTarget> getTargets(Long surveyId) {
        return targetRepository.findBySurveyIdWithDepartment(surveyId);
    }

    private void validateDraft(Survey survey) {
        if(survey.getStatus() != Survey.SurveyStatus.DRAFT) {
            throw new CustomException(ErrorCode.SURVEY_NOT_EDITABLE);
        }
    }

    private void validateManagerScope(Survey survey, List<Long> departmentIds) {
        User creator = survey.getCreator();

        if(creator.getRole() != User.Role.MANAGER) {
            return ;
        }

        Long managerDeptId = creator.getDepartment().getId();
        boolean ownDept = departmentIds.size() == 1 && departmentIds.get(0).equals(managerDeptId);
        if(!ownDept) {
            throw new CustomException(ErrorCode.INVALID_TARGET_DEPARTMENT_FOR_MANAGER);
        }
    }

    private Survey findSurvey(Long id) {
        return surveyRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));
    }

    private Department findDepartment(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));
    }
}
