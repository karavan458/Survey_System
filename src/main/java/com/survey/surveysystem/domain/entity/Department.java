package com.survey.surveysystem.domain.entity;

import com.survey.surveysystem.global.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Department extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    private Department(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static Department create(String name, String description) {
        return new Department(name, description);
    }

    public void update(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
