package com.survey.surveysystem.domain.service;

import com.survey.surveysystem.domain.entity.Question;
import com.survey.surveysystem.domain.entity.Survey;
import com.survey.surveysystem.domain.repository.question.QuestionRepository;
import com.survey.surveysystem.domain.repository.survey.SurveyRepository;
import com.survey.surveysystem.global.exception.CustomException;
import com.survey.surveysystem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;

    @Transactional
    public Question createQuestion(Long surveyId, String content, Question.QuestionType type,
            boolean isRequired, boolean isSatisfaction) {
        Survey survey = findSurvey(surveyId);
        validateDraft(survey);
        validateSingleSatisfaction(surveyId, isSatisfaction);

        int orderNum = resolveOrderNum(surveyId, isSatisfaction);
        Question question = Question.create(survey, content, type, orderNum, isRequired, isSatisfaction);
        return questionRepository.save(question);
    }

    public List<Question> getQuestions(Long surveyId) {
        return questionRepository.findBySurveyIdOrderByOrderNum(surveyId);
    }

    @Transactional
    public void updateQuestion(Long questionId, String content, boolean isRequired, boolean isSatisfaction) {
        Question question = findById(questionId);
        validateDraft(question.getSurvey());

        boolean satisfaction = isSatisfaction && !question.isSatisfaction();
        if(satisfaction) {
            Long surveyId = question.getSurvey().getId();
            validateSingleSatisfaction(surveyId, true);
            question.moveToEnd(currentMaxOrderNum(surveyId));
        }
        question.update(content, isRequired, isSatisfaction);
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        Question question = findById(questionId);
        validateDraft(question.getSurvey());
        question.delete();
    }

    // -- 검증 --
    private void validateDraft(Survey survey) {
        if(survey.getStatus() != Survey.SurveyStatus.DRAFT) {
            throw new CustomException(ErrorCode.SURVEY_NOT_EDITABLE);
        }
    }

    private void validateSingleSatisfaction(Long surveyId, boolean isSatisfaction) {
        if(isSatisfaction && questionRepository.existsBySurveyIdAndIsSatisfactionTrue(surveyId)) {
            throw new CustomException(ErrorCode.SATISFACTION_QUESTION_INVALID);
        }
    }

    // -- 질문 순번 계산 --
    private int resolveOrderNum(Long surveyId, boolean isSatisfaction) {
        int maxOrderNum = currentMaxOrderNum(surveyId);

        if(isSatisfaction) {
            return maxOrderNum + 1;
        }

        return questionRepository.findBySurveyIdAndIsSatisfactionTrue(surveyId)
                .map(satisfactionQuestion -> {
                    int num = satisfactionQuestion.getOrderNum();
                    satisfactionQuestion.moveToEnd(maxOrderNum);
                    return num;
                })
                .orElse(maxOrderNum + 1);
    }

    private int currentMaxOrderNum(Long surveyId) {
        Integer max = questionRepository.findMaxOrderNumBySurveyId(surveyId);
        return max == null ? 0 : max;
    }

    // -- 조회 --
    private Survey findSurvey(Long id) {
        return surveyRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));
    }

    private Question findById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));
    }
}
