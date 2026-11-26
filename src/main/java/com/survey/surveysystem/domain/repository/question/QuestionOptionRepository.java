package com.survey.surveysystem.domain.repository.question;

import com.survey.surveysystem.domain.entity.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {

    List<QuestionOption> findByQuestionIdOrderByOrderNum(Long questionId);
    List<QuestionOption> findByQuestionIdIn(List<Long> questionIds);

    @Query("select max(o.orderNum) from QuestionOption o where o.question.id = :questionId")
    Integer findMaxOrderNumByQuestionId(Long questionId);
}
