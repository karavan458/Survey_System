package com.survey.surveysystem.service;

import com.survey.surveysystem.domain.entity.Department;
import com.survey.surveysystem.domain.entity.Question;
import com.survey.surveysystem.domain.entity.Survey;
import com.survey.surveysystem.domain.entity.User;
import com.survey.surveysystem.domain.repository.question.QuestionRepository;
import com.survey.surveysystem.domain.repository.survey.SurveyRepository;
import com.survey.surveysystem.domain.service.QuestionService;
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
public class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    SurveyRepository surveyRepository;

    @InjectMocks
    private QuestionService questionService;

    private Survey draftSurvey() {
        User creator = User.create("test@test.com", "encodedPw", "홍길동", User.Role.MANAGER,
                Department.create("개발팀", "백엔드/프론트"), "팀장", LocalDate.now());
        return Survey.create("제목", "설명", false, LocalDate.now(), LocalDate.now().plusDays(7), creator);
    }

    @Test
    @DisplayName("DRAFT 설문에 문항을 생성하면 저장된 문항을 반환")
    void createQuestion() {
        // given
        Survey survey = draftSurvey();
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));
        when(questionRepository.findMaxOrderNumBySurveyId(1L)).thenReturn(null);
        when(questionRepository.findBySurveyIdAndIsSatisfactionTrue(1L)).thenReturn(Optional.empty());
        when(questionRepository.save(any(Question.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Question result = questionService.createQuestion(1L, "설문의 만족도 확인", Question.QuestionType.RATING, true, false);

        // then
        assertThat(result.getContent()).isEqualTo("설문의 만족도 확인");
        assertThat(result.getOrderNum()).isEqualTo(1);
    }

    @Test
    @DisplayName("DRAFT 상태가 아닌 설문에 문항을 생성하면 예외 발생")
    void createQuestion_notDRAFT() {
        // given
        Survey survey = draftSurvey();
        survey.changeStatus(Survey.SurveyStatus.ACTIVE);
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));

        // when, then
        assertThatThrownBy(() -> questionService.createQuestion(1L, "내용", Question.QuestionType.TEXT, true, false))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.SURVEY_NOT_EDITABLE);
    }

    @Test
    @DisplayName("만족도 문항은 반드시 1개만 생성")
    void createQuestion_duplicateSatisfaction() {
        // given
        Survey survey = draftSurvey();
        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));
        when(questionRepository.existsBySurveyIdAndIsSatisfactionTrue(1L)).thenReturn(true);

        // when, then
        assertThatThrownBy(() -> questionService.createQuestion(1L, "만족도2", Question.QuestionType.RATING, true, true))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.SATISFACTION_QUESTION_INVALID);
    }

    @Test
    @DisplayName("새로운 문항이 생성되어도 만족도 문항은 반드시 마지막에 위치")
    void createQuestion_SatisfactionInEnd() {
        // given
        Survey survey = draftSurvey();
        Question satisfaction = Question.create(survey, "만족도 조사", Question.QuestionType.RATING, 1, true, true);

        when(surveyRepository.findById(1L)).thenReturn(Optional.of(survey));
        when(questionRepository.findMaxOrderNumBySurveyId(1L)).thenReturn(1);
        when(questionRepository.findBySurveyIdAndIsSatisfactionTrue(1L)).thenReturn(Optional.of(satisfaction));
        when(questionRepository.save(any(Question.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Question newQuestion = questionService.createQuestion(1L, "건의 사항", Question.QuestionType.TEXT, false, false);

        // then
        assertThat(newQuestion.getOrderNum()).isEqualTo(1);
        assertThat(satisfaction.getOrderNum()).isEqualTo(2);
    }

    @Test
    @DisplayName("기존 문항을 수정해 만족도로 변경해도 만족도 문항은 반드시 마지막에 위치")
    void updateQuestion_SatisfactionInEnd() {
        // given
        Survey survey = draftSurvey();
        Question question = Question.create(survey, "내용", Question.QuestionType.TEXT, 1, true, false);

        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(questionRepository.existsBySurveyIdAndIsSatisfactionTrue(survey.getId())).thenReturn(false);
        when(questionRepository.findMaxOrderNumBySurveyId(survey.getId())).thenReturn(3);

        // when
        questionService.updateQuestion(1L, "만족도로 수정된 질문", true, true);

        // then
        assertThat(question.getOrderNum()).isEqualTo(4);
        assertThat(question.isSatisfaction()).isTrue();
    }

    @Test
    @DisplayName("문항을 삭제하면 deleteAt에 값 입력")
    void deleteQuestion() {
        // given
        Survey survey = draftSurvey();
        Question question = Question.create(survey, "내용", Question.QuestionType.TEXT, 1, true, false);
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        // when
        questionService.deleteQuestion(1L);

        // then
        assertThat(question.isDeleted()).isTrue();
    }
}
