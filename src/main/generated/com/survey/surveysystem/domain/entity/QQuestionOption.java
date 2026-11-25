package com.survey.surveysystem.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestionOption is a Querydsl query type for QuestionOption
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestionOption extends EntityPathBase<QuestionOption> {

    private static final long serialVersionUID = 1486244058L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestionOption questionOption = new QQuestionOption("questionOption");

    public final com.survey.surveysystem.global.QBaseEntity _super = new com.survey.surveysystem.global.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> orderNum = createNumber("orderNum", Integer.class);

    public final QQuestion question;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QQuestionOption(String variable) {
        this(QuestionOption.class, forVariable(variable), INITS);
    }

    public QQuestionOption(Path<? extends QuestionOption> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestionOption(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestionOption(PathMetadata metadata, PathInits inits) {
        this(QuestionOption.class, metadata, inits);
    }

    public QQuestionOption(Class<? extends QuestionOption> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.question = inits.isInitialized("question") ? new QQuestion(forProperty("question"), inits.get("question")) : null;
    }

}

