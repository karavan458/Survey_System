package com.survey.surveysystem.service;

import com.survey.surveysystem.domain.entity.Department;
import com.survey.surveysystem.domain.entity.Survey;
import com.survey.surveysystem.domain.entity.SurveyTarget;
import com.survey.surveysystem.domain.entity.User;
import com.survey.surveysystem.domain.repository.department.DepartmentRepository;
import com.survey.surveysystem.domain.repository.survey.SurveyRepository;
import com.survey.surveysystem.domain.repository.survey.SurveyTargetRepository;
import com.survey.surveysystem.domain.service.SurveyTargetService;
import com.survey.surveysystem.global.exception.CustomException;
import com.survey.surveysystem.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SurveyTargetServiceTest {

    @Mock
    private SurveyTargetRepository targetRepository;

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private SurveyTargetService targetService;

    private Survey draftSurveyByAdmin() {
        Department dept = Department.create("경영실", "경영 지원 / 인사 관리");
        User admin = User.create("admin@test.com", "encodedPw", "관리자", User.Role.ADMIN, dept, "대표", LocalDate.now());
        return Survey.create("제목", "내용", false, LocalDate.now(), LocalDate.now().plusDays(7), admin);
    }

    @Test
    @DisplayName("DRAFT 상태가 아닌 설문의 타겟을 설정하려 하면 예외가 발생")
    void setTarget_notDRAFT() {
        // given
        Survey survey = draftSurveyByAdmin();
        survey.changeStatus(Survey.SurveyStatus.ACTIVE);
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));

        // when, then
        assertThatThrownBy(() -> targetService.setTargets(1L, List.of(1L)))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.SURVEY_NOT_EDITABLE);
    }

    @Test
    @DisplayName("ADMIN은 여러 부서를 타겟으로 설정 가능")
    void setTargets_admin_multipleDepartment() {
        // given
        Survey survey = draftSurveyByAdmin();
        Department dev = Department.create("개발팀", "백엔드 / 프론트");
        Department plan = Department.create("기획팀", "PM / PL");

        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dev));
        when(departmentRepository.findById(2L)).thenReturn(Optional.of(plan));

        // when
        targetService.setTargets(1L, List.of(1L, 2L));

        // then
        verify(targetRepository).deleteBySurveyId(1L);
        ArgumentCaptor<List<SurveyTarget>> captor = ArgumentCaptor.forClass(List.class);
        verify(targetRepository).saveAll(captor.capture());
        assertThat(captor.getValue()).hasSize(2);
    }

    @Test
    @DisplayName("MANAGER는 본인 부서가 아닌 부서를 타겟 지정 불가")
    void setTargets_ManagerOtherDept_throws() {
        // given
        Department managerDept = Department.create("개발팀", "백엔드 / 프론트");
        User manager = User.create("mamager@Test.com", "encodedPw", "매니저", User.Role.MANAGER, managerDept, "팀장", LocalDate.now());
        Survey survey = Survey.create("제목", "내용", false, LocalDate.now(), LocalDate.now().plusDays(7), manager);
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));

        // when, then
        assertThatThrownBy(() -> targetService.setTargets(1L, List.of(999L)))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_TARGET_DEPARTMENT_FOR_MANAGER);
    }

    @Test
    @DisplayName("MANAGER는 본인 부서만 타겟으로 지정 가능")
    void setTargets_ManagerOwnDept_Success() {
        // given
        Department managerDept = Department.create("개발팀", "백엔드 / 프론트");
        ReflectionTestUtils.setField(managerDept, "id", 10L);
        User manager = User.create("mamager@Test.com", "encodedPw", "매니저", User.Role.MANAGER, managerDept, "팀장", LocalDate.now());
        Survey survey = Survey.create("제목", "내용", false, LocalDate.now(), LocalDate.now().plusDays(7), manager);

        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));
        when(departmentRepository.findById(10L)).thenReturn(Optional.of(managerDept));

        // when
        targetService.setTargets(1L, List.of(10L));

        // then
        verify(targetRepository).deleteBySurveyId(1L);
        verify(targetRepository).saveAll(anyList());
    }
}
