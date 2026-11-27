package com.survey.surveysystem.domain.service;

import com.survey.surveysystem.domain.entity.Question;
import com.survey.surveysystem.domain.entity.QuestionOption;
import com.survey.surveysystem.domain.entity.Survey;
import com.survey.surveysystem.domain.entity.User;
import com.survey.surveysystem.domain.repository.question.QuestionOptionRepository;
import com.survey.surveysystem.domain.repository.question.QuestionRepository;
import com.survey.surveysystem.domain.repository.survey.SurveyRepository;
import com.survey.surveysystem.domain.repository.user.UserRepository;
import com.survey.surveysystem.global.exception.CustomException;
import com.survey.surveysystem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository optionRepository;

    @Transactional
    public Survey createSurvey(String title, String description, boolean isAnonymous, LocalDate startDate, LocalDate endDate, Long creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));

        Survey survey = Survey.create(title, description, isAnonymous, startDate, endDate, creator);
        return surveyRepository.save(survey);
    }

    public List<Survey> getSurveys() {
        return surveyRepository.findAllWithCreator();
    }

    @Transactional
    public void updateSurvey(Long id, String title, String description, LocalDate startDate, LocalDate endDate) {
        Survey survey = findById(id);
        survey.update(title, description, startDate, endDate);
    }

    @Transactional
    public void changeStatus(Long id, Survey.SurveyStatus status) {
        Survey survey = findById(id);
        if(status == Survey.SurveyStatus.ACTIVE) {
            validateActivatable(id);
        }
        survey.changeStatus(status);
    }

    @Transactional
    public void deleteSurvey(Long id) {
        Survey survey = findById(id);
        survey.delete();
    }

    public Survey getSurvey(Long id) {
        return surveyRepository.findByIdWithCreator(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));
    }

    private Survey findById(Long id) {
        return surveyRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));
    }

    private void validateActivatable(Long surveyId) {
        if(!questionRepository.existsBySurveyIdAndIsSatisfactionTrue(surveyId)) {
            throw new CustomException(ErrorCode.SATISFACTION_QUESTION_INVALID);
        }

        List<Question> questions = questionRepository.findBySurveyIdOrderByOrderNum(surveyId);
        List<Long> choiceQuestionIds = questions.stream()
                .filter(q -> q.getType() == Question.QuestionType.SINGLE_CHOICE ||
                        q.getType() == Question.QuestionType.MULTI_CHOICE)
                .map(Question::getId)
                .toList();

        if(choiceQuestionIds.isEmpty()) {
            return;
        }

        Map<Long, List<QuestionOption>> optionLookup = optionRepository.findByQuestionIdIn(choiceQuestionIds).stream()
                .collect(Collectors.groupingBy(option -> option.getQuestion().getId()));

        boolean hasEmptyChoice = choiceQuestionIds.stream()
                .anyMatch(qid -> optionLookup.getOrDefault(qid, List.of()).isEmpty());

        if(hasEmptyChoice) {
            throw new CustomException(ErrorCode.QUESTION_OPTION_REQUIRED);
        }
    }
}
