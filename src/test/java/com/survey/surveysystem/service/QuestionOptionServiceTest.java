package com.survey.surveysystem.service;

import com.survey.surveysystem.domain.entity.*;
import com.survey.surveysystem.domain.repository.question.QuestionOptionRepository;
import com.survey.surveysystem.domain.repository.question.QuestionRepository;
import com.survey.surveysystem.domain.service.QuestionOptionService;
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
public class QuestionOptionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuestionOptionRepository optionRepository;

    @InjectMocks
    private QuestionOptionService optionService;

    private Survey draftSurvey() {
        User creator = User.create("test@company.com", "encodedPw", "홍길동",
                User.Role.MANAGER, Department.create("개발팀", "백엔드 / 프론트엔드"),
                "팀장", LocalDate.now());
        return Survey.create("제목", "설명", false, LocalDate.now(), LocalDate.now().plusDays(7), creator);
    }

    @Test
    @DisplayName("DRAFT 설문의 문항에 옵션을 생성하면 저장된 옵션을 반환")
    void createOption() {
        // given
        Survey survey = draftSurvey();
        Question question = Question.create(survey, "현재 사용중인 OS는?", Question.QuestionType.SINGLE_CHOICE, 1, true, false);

        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(optionRepository.findMaxOrderNumByQuestionId(1L)).thenReturn(null);
        when(optionRepository.save(any(QuestionOption.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        QuestionOption result = optionService.createOption(1L, "Window");

        // then
        assertThat(result.getContent()).isEqualTo("Window");
        assertThat(result.getOrderNum()).isEqualTo(1);
    }

    @Test
    @DisplayName("DRAFT 상태가 아닌 질문의 옵션을 생성하는 것은 불가")
    void createOption_notDRAFT() {
        // given
        Survey survey = draftSurvey();
        survey.changeStatus(Survey.SurveyStatus.ACTIVE);
        Question question = Question.create(survey, "현재 사용중인 OS는?", Question.QuestionType.SINGLE_CHOICE, 1, true, false);
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        // when, then
        assertThatThrownBy(() -> optionService.createOption(1L, "Window"))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.SURVEY_NOT_EDITABLE);
    }

    @Test
    @DisplayName("옵선 내용을 수정하면 반영")
    void updateOption() {
        // given
        Survey survey = draftSurvey();
        Question question = Question.create(survey, "현재 사용중인 OS는?", Question.QuestionType.SINGLE_CHOICE, 1, true, false);
        QuestionOption option = QuestionOption.create(question, "Window", 1);
        when(optionRepository.findById(1L)).thenReturn(Optional.of(option));

        // when
        optionService.updateOption(1L, "Mac OS");

        // then
        assertThat(option.getContent()).isEqualTo("Mac OS");
    }

    @Test
    @DisplayName("옵션을 삭제하면 deletedAt에 값 입력")
    void deleteOption() {
        // given
        Survey survey = draftSurvey();
        Question question = Question.create(survey, "현재 사용 중인 OS는?", Question.QuestionType.SINGLE_CHOICE, 1, true, false);
        QuestionOption option = QuestionOption.create(question, "Windows", 1);
        when(optionRepository.findById(1L)).thenReturn(Optional.of(option));

        // when
        optionService.deleteOption(1L);

        // then
        assertThat(option.isDeleted()).isTrue();
    }
}
