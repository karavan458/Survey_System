package com.survey.surveysystem.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSurveyTarget is a Querydsl query type for SurveyTarget
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSurveyTarget extends EntityPathBase<SurveyTarget> {

    private static final long serialVersionUID = -1364073942L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSurveyTarget surveyTarget = new QSurveyTarget("surveyTarget");

    public final QDepartment department;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QSurvey survey;

    public QSurveyTarget(String variable) {
        this(SurveyTarget.class, forVariable(variable), INITS);
    }

    public QSurveyTarget(Path<? extends SurveyTarget> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSurveyTarget(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSurveyTarget(PathMetadata metadata, PathInits inits) {
        this(SurveyTarget.class, metadata, inits);
    }

    public QSurveyTarget(Class<? extends SurveyTarget> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.department = inits.isInitialized("department") ? new QDepartment(forProperty("department")) : null;
        this.survey = inits.isInitialized("survey") ? new QSurvey(forProperty("survey"), inits.get("survey")) : null;
    }

}

