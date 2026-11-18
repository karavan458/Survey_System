package com.survey.surveysystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SurveySystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SurveySystemApplication.class, args);
    }

}
