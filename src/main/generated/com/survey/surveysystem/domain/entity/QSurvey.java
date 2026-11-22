package com.survey.surveysystem.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSurvey is a Querydsl query type for Survey
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSurvey extends EntityPathBase<Survey> {

    private static final long serialVersionUID = -1505389767L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSurvey survey = new QSurvey("survey");

    public final com.survey.surveysystem.global.QBaseEntity _super = new com.survey.surveysystem.global.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QUser creator;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final StringPath description = createString("description");

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isAnonymous = createBoolean("isAnonymous");

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final EnumPath<Survey.SurveyStatus> status = createEnum("status", Survey.SurveyStatus.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSurvey(String variable) {
        this(Survey.class, forVariable(variable), INITS);
    }

    public QSurvey(Path<? extends Survey> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSurvey(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSurvey(PathMetadata metadata, PathInits inits) {
        this(Survey.class, metadata, inits);
    }

    public QSurvey(Class<? extends Survey> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.creator = inits.isInitialized("creator") ? new QUser(forProperty("creator"), inits.get("creator")) : null;
    }

}

