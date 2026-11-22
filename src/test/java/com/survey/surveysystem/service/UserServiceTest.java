package com.survey.surveysystem.service;

import com.survey.surveysystem.domain.entity.Department;
import com.survey.surveysystem.domain.entity.User;
import com.survey.surveysystem.domain.repository.department.DepartmentRepository;
import com.survey.surveysystem.domain.repository.user.UserRepository;
import com.survey.surveysystem.domain.service.user.UserService;
import com.survey.surveysystem.global.exception.CustomException;
import com.survey.surveysystem.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("유저를 생성하면 저장된 유저를 반환한다.")
    void createUser() {
        Department department = Department.create("개발팀", "백엔드/프론트엔드");
        User user = User.create("test@test.com", "encodePassword", "홍길동",
                User.Role.EMPLOYEE, department, "사원", LocalDate.of(2026, 1, 1));

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(passwordEncoder.encode("rawPw")).thenReturn("encodedPw");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createUser("test@test.com", "rawPw", "홍길동",
                User.Role.EMPLOYEE, 1L, "사원", LocalDate.of(2026, 1, 1));

        assertThat(result.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("중복된 이메일은 사용할 수 없음")
    void createUser_duplicateEmail() {
        User user = User.create("test@test.com", "encodedPw", "홍길동",
                User.Role.EMPLOYEE, null, "사원", LocalDate.now());
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.createUser("test@test.com", "rawPw", "홍길동",
                User.Role.EMPLOYEE, 1L, "사원", LocalDate.now()))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.DUPLICATE_EMAIL);
    }

    @Test
    @DisplayName("존재하지 않는 부서로 유저를 생성하려 하면 예외 발생")
    void createUser_departmentNotFound() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());
        when(departmentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.createUser("test@test.com", "encodedPw", "홍길동",
                User.Role.EMPLOYEE, 999L, "사원", LocalDate.now()))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.ENTITY_NOT_FOUND);
    }

    @Test
    @DisplayName("부서를 변경하면 유저의 부서가 변경")
    void changeDepartment() {
        Department oldDep = Department.create("개발팀", "백엔드/프론트엔드");
        Department newDep = Department.create("기획팀", "PM/PL");
        User user = User.create("test@test.com", "encodedPw", "홍길동",
                User.Role.EMPLOYEE, oldDep, "사원", LocalDate.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(departmentRepository.findById(2L)).thenReturn(Optional.of(newDep));

        userService.changeDepartment(1L, 2L);

        assertThat(user.getDepartment()).isEqualTo(newDep);
    }

    @Test
    @DisplayName("유저를 삭제하면 deletedAt에 값 입력")
    void deleteUser() {
        User user = User.create("test@test.com", "encodedPw", "홍길동",
                User.Role.EMPLOYEE, null, "사원", LocalDate.now());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        assertThat(user.isDeleted()).isTrue();
    }
}
