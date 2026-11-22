package com.survey.surveysystem.web.controller;

import com.survey.surveysystem.domain.entity.Department;
import com.survey.surveysystem.domain.service.department.DepartmentService;
import com.survey.surveysystem.web.dto.department.DepartmentCreateRequest;
import com.survey.surveysystem.web.dto.department.DepartmentResponse;
import com.survey.surveysystem.web.dto.department.DepartmentUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentResponse> createDepartment(@Valid @RequestBody DepartmentCreateRequest request) {
        Department department = departmentService.createDepartment(request.name(), request.description());
        return ResponseEntity.status(HttpStatus.CREATED).body(DepartmentResponse.from(department));
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> getDepartments() {
        List<DepartmentResponse> responses = departmentService.getDepartments().stream()
                .map(DepartmentResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDepartment(@PathVariable Long id, @Valid @RequestBody DepartmentUpdateRequest request) {
        departmentService.updateDepartment(id, request.name(), request.description());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
