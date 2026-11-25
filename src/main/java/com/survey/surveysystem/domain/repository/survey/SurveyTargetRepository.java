package com.survey.surveysystem.domain.repository.survey;

import com.survey.surveysystem.domain.entity.SurveyTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SurveyTargetRepository extends JpaRepository<SurveyTarget, Long> {

    List<SurveyTarget> findBySurveyId(Long surveyId);
    void deleteBySurveyId(Long surveyId);

    @Query("select t from SurveyTarget t left join fetch t.department where t.survey.id = :surveyId")
    List<SurveyTarget> findBySurveyIdWithDepartment(Long surveyId);
}
