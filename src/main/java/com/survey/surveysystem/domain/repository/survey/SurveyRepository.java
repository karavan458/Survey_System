package com.survey.surveysystem.domain.repository.survey;

import com.survey.surveysystem.domain.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

    @Query("select s from Survey s left join fetch s.creator")
    List<Survey> findAllWithCreator();
}
