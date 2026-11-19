package com.survey.surveysystem.service;

import com.survey.surveysystem.domain.entity.Department;
import com.survey.surveysystem.domain.repository.department.DepartmentRepository;
import com.survey.surveysystem.domain.service.department.DepartmentService;
import com.survey.surveysystem.global.exception.CustomException;
import com.survey.surveysystem.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    @DisplayName("부서를 생성하면 저장된 부서를 반환")
    void createDepartment() {

        // given
        Department department = Department.create("개발팀", "백엔드/프론트엔드");
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        // when
        Department result = departmentService.createDepartment("개발팀", "백엔드/프론트엔드");

        // then
        assertThat(result.getName()).isEqualTo("개발팀");
    }

    @Test
    @DisplayName("존재하지 않는 부서를 수정하려 들면 예외 발생")
    void updateDepartmentNotFound() {

        // given
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> departmentService.updateDepartment(1L, "이름", "설명"))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.ENTITY_NOT_FOUND);
    }

    @Test
    @DisplayName("부서를 삭제하면 deletedAt이 채워진다.")
    void deleteDepartment() {

        // given
        Department department = Department.create("개발팀", "백엔드/프론트엔드");
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        // when
        departmentService.deleteDepartment(1L);

        // then
        assertThat(department.isDeleted()).isTrue();
    }
}
