package com.survey.surveysystem.domain.entity;

import com.survey.surveysystem.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    private String content;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    private int orderNum;

    private boolean isRequired;

    private boolean isSatisfaction;

    private Question(Survey survey, String content, QuestionType type, int orderNum, boolean isRequired, boolean isSatisfaction) {
        this.survey = survey;
        this.content = content;
        this.type = type;
        this.orderNum = orderNum;
        this.isRequired = isRequired;
        this.isSatisfaction = isSatisfaction;
    }

    public static Question create(Survey survey, String content, QuestionType type, int orderNum, boolean isRequired, boolean isSatisfaction) {
        return new Question(survey, content, type, orderNum, isRequired, isSatisfaction);
    }

    public void update(String content, boolean isRequired, boolean isSatisfaction) {
        this.content = content;
        this.isRequired = isRequired;
        this.isSatisfaction = isSatisfaction;
    }

    public enum QuestionType {
        SINGLE_CHOICE, MULTI_CHOICE, TEXT, RATING
    }

    /**
     * 내부용 - QuestionService가 만족도 문항을 재배치할 때만 호출할 것.
     * 외부에서 임의로 순서로 바꾸는 용도로 사용하지 말 것.
     */
    public void moveToEnd(int currentMaxOrderNum) {
        this.orderNum = currentMaxOrderNum + 1;
    }
}
