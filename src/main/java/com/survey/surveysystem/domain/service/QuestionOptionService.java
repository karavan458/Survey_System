package com.survey.surveysystem.domain.service;

import com.survey.surveysystem.domain.entity.Question;
import com.survey.surveysystem.domain.entity.QuestionOption;
import com.survey.surveysystem.domain.entity.Survey;
import com.survey.surveysystem.domain.repository.question.QuestionOptionRepository;
import com.survey.surveysystem.domain.repository.question.QuestionRepository;
import com.survey.surveysystem.global.exception.CustomException;
import com.survey.surveysystem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionOptionService {

    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository optionRepository;

    @Transactional
    public QuestionOption createOption(Long questionId, String content) {
        Question question = findQuestion(questionId);
        validateDraft(question.getSurvey());

        int orderNum = currentMaxOrderNum(questionId) + 1;
        QuestionOption option = QuestionOption.create(question, content, orderNum);
        return optionRepository.save(option);
    }

    public List<QuestionOption> getOptions(Long questionId) {
        return optionRepository.findByQuestionIdOrderByOrderNum(questionId);
    }

    @Transactional
    public void updateOption(Long optionId, String content) {
        QuestionOption option = findById(optionId);
        validateDraft(option.getQuestion().getSurvey());
        option.update(content);
    }

    @Transactional
    public void deleteOption(Long optionId) {
        QuestionOption option = findById(optionId);
        validateDraft(option.getQuestion().getSurvey());
        option.delete();
    }

    private void validateDraft(Survey survey) {
        if(survey.getStatus() != Survey.SurveyStatus.DRAFT) {
            throw new CustomException(ErrorCode.SURVEY_NOT_EDITABLE);
        }
    }

    private int currentMaxOrderNum(Long questionId) {
        Integer max = optionRepository.findMaxOrderNumByQuestionId(questionId);
        return max == null ? 0 : max;
    }

    private Question findQuestion(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));
    }

    private QuestionOption findById(Long optionId) {
        return optionRepository.findById(optionId)
                .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));
    }
}
