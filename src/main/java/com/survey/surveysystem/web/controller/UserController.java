package com.survey.surveysystem.web.controller;

import com.survey.surveysystem.domain.entity.User;
import com.survey.surveysystem.domain.service.UserService;
import com.survey.surveysystem.web.dto.user.request.UserCreateRequest;
import com.survey.surveysystem.web.dto.user.request.UserDepartmentChangeRequest;
import com.survey.surveysystem.web.dto.user.request.UserPositionChangeRequest;
import com.survey.surveysystem.web.dto.user.request.UserRoleChangeRequest;
import com.survey.surveysystem.web.dto.user.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        User user = userService.createUser(
                request.email(), request.password(), request.name(),
                request.role(), request.departmentId(), request.position(), request.hireDate()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<UserResponse> userList = userService.getUsers().stream()
                .map(UserResponse::from)
                .toList();

        return ResponseEntity.ok(userList);
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<Void> changeRole(@PathVariable Long id, @Valid @RequestBody UserRoleChangeRequest request) {
        userService.changeRole(id, request.role());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/department")
    public ResponseEntity<Void> changeDepartment(@PathVariable Long id, @Valid @RequestBody UserDepartmentChangeRequest request) {
        userService.changeDepartment(id, request.departmentId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/position")
    public ResponseEntity<Void> changePosition(@PathVariable Long id, @Valid @RequestBody UserPositionChangeRequest request) {
        userService.changePosition(id, request.position());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
