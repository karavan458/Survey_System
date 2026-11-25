package com.survey.surveysystem.domain.service;

import com.survey.surveysystem.domain.entity.Department;
import com.survey.surveysystem.domain.repository.department.DepartmentRepository;
import com.survey.surveysystem.global.exception.CustomException;
import com.survey.surveysystem.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Transactional
    public Department createDepartment(String name, String description) {
        Department department = Department.create(name, description);
        return departmentRepository.save(department);
    }

    public List<Department> getDepartments() {
        return departmentRepository.findAll();
    }

    @Transactional
    public void updateDepartment(Long id, String name, String description) {
        Department department = findById(id);
        department.update(name, description);
    }

    @Transactional
    public void deleteDepartment(Long id) {
        Department department = findById(id);
        department.delete();
    }

    private Department findById(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));
    }
}
