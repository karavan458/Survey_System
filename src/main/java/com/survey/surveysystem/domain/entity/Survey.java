package com.survey.surveysystem.domain.entity;


import com.survey.surveysystem.global.BaseEntity;
import com.survey.surveysystem.global.exception.CustomException;
import com.survey.surveysystem.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Getter
@Entity
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Survey extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private SurveyStatus status;

    private boolean isAnonymous;

    private LocalDate startDate;

    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    private Survey(String title, String description, boolean isAnonymous, LocalDate startDate, LocalDate endDate, User creator) {
        this.title = title;
        this.description = description;
        this.status = SurveyStatus.DRAFT;
        this.isAnonymous = isAnonymous;
        this.startDate = startDate;
        this.endDate = endDate;
        this.creator = creator;
    }

    public static Survey create(String title, String description, boolean isAnonymous, LocalDate startDate, LocalDate endDate, User creator) {
        return new Survey(title, description, isAnonymous, startDate, endDate, creator);
    }

    public void update(String title, String description, LocalDate startDate, LocalDate endDate) {
        if(this.status != SurveyStatus.DRAFT) {
            throw new CustomException(ErrorCode.SURVEY_NOT_EDITABLE);
        }

        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void changeStatus(SurveyStatus newStatus) {
        if(!this.status.canTransitionTo(newStatus)) {
            throw new CustomException(ErrorCode.INVALID_SURVEY_TRANSITION);
        }

        this.status = newStatus;
    }

    public enum SurveyStatus {
        DRAFT {
            @Override
            public boolean canTransitionTo(SurveyStatus target) {
                return target == ACTIVE;
            }
        },

        ACTIVE {
            @Override
            public boolean canTransitionTo(SurveyStatus target) {
                return target == CLOSED || target == DRAFT;
            }
        },

        CLOSED {
            @Override
            public boolean canTransitionTo(SurveyStatus target) {
                return false;
            }
        };

        public abstract boolean canTransitionTo(SurveyStatus target);
    }
}
