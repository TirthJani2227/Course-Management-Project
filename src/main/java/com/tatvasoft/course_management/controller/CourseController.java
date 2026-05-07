package com.tatvasoft.course_management.controller;


import com.tatvasoft.course_management.dto.request.CourseManagementDTO.Create;
import com.tatvasoft.course_management.dto.response.ApiResponseDTO;
import com.tatvasoft.course_management.dto.response.CourseResponseDTO;
import com.tatvasoft.course_management.entity.Course;
import com.tatvasoft.course_management.entity.User;
import com.tatvasoft.course_management.enums.Department;
import com.tatvasoft.course_management.exception.UserNotFoundException;
import com.tatvasoft.course_management.service.CourseManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CourseController {

    private final CourseManagementService courseManagementService;

    @PostMapping("/course")
    public ResponseEntity<ApiResponseDTO<CourseResponseDTO>> createCourse(@RequestBody @Valid Create dto, Authentication authentication) {
        User admin = (User) authentication.getPrincipal();
        if (admin == null)
            throw new UserNotFoundException("Admin not found");
        CourseResponseDTO course = courseManagementService.createCourse(dto.getName(), dto.getContent(), dto.getDepartment(), dto.getCredits(), admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success("New course created successfully", course));
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<ApiResponseDTO<CourseResponseDTO>> getCourseById(@PathVariable Long id) {
        CourseResponseDTO course = courseManagementService.getCourseById(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Course retrieved successfully", course));
    }

    @GetMapping("/course")
    public ResponseEntity<ApiResponseDTO<Page<CourseResponseDTO>>> getAllCourses(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Department department,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean ascending) {

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CourseResponseDTO> courses = courseManagementService.getAllCourses(name, department, pageable);
        return ResponseEntity.ok(ApiResponseDTO.success("Courses list retrieved successfully", courses));
    }

    @PatchMapping("/course/{id}")
    public ResponseEntity<ApiResponseDTO<CourseResponseDTO>> updateCourseById(@PathVariable Long id, @RequestBody Create dto) {
        CourseResponseDTO course = courseManagementService.updateCourseById(id, dto);
        return ResponseEntity.ok(ApiResponseDTO.success("Course updated successfully", course));
    }

    @DeleteMapping("/course/{id}")
    public ResponseEntity<ApiResponseDTO<Boolean>> deleteCourseById(@PathVariable Long id) {
        boolean isDeleted = courseManagementService.deleteCourseById(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Course deleted successfully", isDeleted));
    }
}
