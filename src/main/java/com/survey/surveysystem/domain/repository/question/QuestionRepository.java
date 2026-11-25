package com.survey.surveysystem.domain.repository.question;

import com.survey.surveysystem.domain.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findBySurveyIdOrderByOrderNum(Long surveyId);
    boolean existsBySurveyIdAndIsSatisfactionTrue(Long surveyId);
    Optional<Question> findBySurveyIdAndIsSatisfactionTrue(Long surveyId);

    @Query("select max(q.orderNum) from Question q where q.survey.id = :surveyId")
    Integer findMaxOrderNumBySurveyId(Long surveyId);
}
