package com.survey.surveysystem.domain.service.user;

import com.survey.surveysystem.domain.entity.Department;
import com.survey.surveysystem.domain.entity.User;
import com.survey.surveysystem.domain.repository.department.DepartmentRepository;
import com.survey.surveysystem.domain.repository.user.UserRepository;
import com.survey.surveysystem.global.exception.CustomException;
import com.survey.surveysystem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(String email, String rawPassword, String name, User.Role role, Long departmentId, String position, LocalDate hireDate) {
        if(userRepository.findByEmail(email).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));

        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = User.create(email, encodedPassword, name, role, department, position, hireDate);
        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAllWithDepartment();
    }

    @Transactional
    public void changeRole(Long id, User.Role role) {
        User user = findById(id);
        user.changeRole(role);
    }

    @Transactional
    public void changeDepartment(Long id, Long departmentId) {
        User user = findById(id);
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));

        user.changeDepartment(department);
    }

    @Transactional
    public void changePosition(Long id, String position) {
        User user = findById(id);
        user.changePosition(position);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findById(id);
        user.delete();
    }

    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));
    }
}
