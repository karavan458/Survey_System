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
public class QuestionOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    private String content;

    private int orderNum;

    private QuestionOption(Question question, String content, int orderNum) {
        this.question = question;
        this.content = content;
        this.orderNum = orderNum;
    }

    public static QuestionOption create(Question question, String content, int orderNum) {
        return new QuestionOption(question, content, orderNum);
    }

    public void update(String content) {
        this.content = content;
    }
}
