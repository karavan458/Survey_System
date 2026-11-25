package com.survey.surveysystem.service;

import com.survey.surveysystem.domain.entity.Department;
import com.survey.surveysystem.domain.entity.Survey;
import com.survey.surveysystem.domain.entity.User;
import com.survey.surveysystem.domain.repository.survey.SurveyRepository;
import com.survey.surveysystem.domain.repository.user.UserRepository;
import com.survey.surveysystem.domain.service.SurveyService;
import com.survey.surveysystem.global.exception.CustomException;
import com.survey.surveysystem.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SurveyServiceTest {

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SurveyService surveyService;

    private User creator() {
        Department department = Department.create("개발팀", "백엔드/프론트");
        return User.create("test@test.com", "encodedPw", "홍길동",
                User.Role.MANAGER, department, "팀장", LocalDate.now());
    }

    @Test
    @DisplayName("설문을 생성하면 생성된 설문이 반환")
    void createSurvey() {
        // given
        User creator = creator();
        Survey survey = Survey.create("제목", "설명", false, LocalDate.now(), LocalDate.now().plusDays(7), creator);

        when(userRepository.findById(1L)).thenReturn(Optional.of(creator));
        when(surveyRepository.save(any(Survey.class))).thenReturn(survey);

        // when
        Survey result = surveyService.createSurvey("제목", "설명", false, LocalDate.now(), LocalDate.now().plusDays(7), 1L);

        // then
        assertThat(result.getTitle()).isEqualTo("제목");
    }

    @Test
    @DisplayName("존재하지 않은 작성자는 설문을 작성하려 하면 예외 발생")
    void createSurvey_creatorNotFound() {
        // given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> surveyService.createSurvey("제목", "설명", false, LocalDate.now(), LocalDate.now().plusDays(7), 999L))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.ENTITY_NOT_FOUND);
    }

    @Test
    @DisplayName("DRAFT 상태의 설문은 ACTIVE로 상태 변경 가능")
    void changeStatus_DRAFT_TO_ACTIVE() {
        // given
        Survey survey = Survey.create("제목", "설명", false, LocalDate.now(), LocalDate.now().plusDays(7), creator());
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));

        // when
        surveyService.changeStatus(1L, Survey.SurveyStatus.ACTIVE);

        // then
        assertThat(survey.getStatus()).isEqualTo(Survey.SurveyStatus.ACTIVE);
    }

    @Test
    @DisplayName("DRAFT 상태의 설문은 수정 가능")
    void updateSurvey_whenDRAFT() {
        // given
        Survey survey = Survey.create("제목", "설명", false, LocalDate.now(), LocalDate.now().plusDays(7), creator());
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));

        // when
        surveyService.updateSurvey(1L, "새 제목", "새 설명", LocalDate.now(), LocalDate.now().plusDays(14));

        // then
        assertThat(survey.getTitle()).isEqualTo("새 제목");
    }

    @Test
    @DisplayName("ACTIVE 상태의 설문은 수정 불가")
    void updateSurvey_whenACTIVE() {
        // given
        Survey survey = Survey.create("제목", "설명", false, LocalDate.now(), LocalDate.now().plusDays(7), creator());
        survey.changeStatus(Survey.SurveyStatus.ACTIVE);
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));

        // when, then
        assertThatThrownBy(() -> surveyService.updateSurvey(1L, "새 제목", "새 설명",
                LocalDate.now(), LocalDate.now().plusDays(14)))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.SURVEY_NOT_EDITABLE);
    }

    @Test
    @DisplayName("CLOSED 상태의 설문은 상태 변경 불가")
    void changeStatus_whenCLOSED() {
        // given
        Survey survey = Survey.create("제목", "설명", false, LocalDate.now(), LocalDate.now().plusDays(7), creator());
        survey.changeStatus(Survey.SurveyStatus.ACTIVE);
        survey.changeStatus(Survey.SurveyStatus.CLOSED);
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));

        // when, then
        assertThatThrownBy(() -> surveyService.changeStatus(1L, Survey.SurveyStatus.DRAFT))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_SURVEY_TRANSITION);
    }

    @Test
    @DisplayName("설문을 삭제하면 deletedAt에 값이 채워짐")
    void deleteSurvey() {
        // given
        Survey survey = Survey.create("제목", "설명", false, LocalDate.now(), LocalDate.now().plusDays(7), creator());
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));

        // when
        surveyService.deleteSurvey(1L);

        // then
        assertThat(survey.isDeleted()).isTrue();
    }
}
