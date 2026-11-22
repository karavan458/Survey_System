package com.survey.surveysystem.domain.repository.user;

import com.survey.surveysystem.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);

    @Query("select u from User u left join fetch u.department")
    List<User> findAllWithDepartment();
}
