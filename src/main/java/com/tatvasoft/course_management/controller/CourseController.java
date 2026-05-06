package com.tatvasoft.course_management.controller;


import com.tatvasoft.course_management.dto.request.CourseManagementDTO.Create;
import com.tatvasoft.course_management.dto.response.ApiResponseDTO;
import com.tatvasoft.course_management.entity.Course;
import com.tatvasoft.course_management.entity.User;
import com.tatvasoft.course_management.exception.UserNotFoundException;
import com.tatvasoft.course_management.service.CourseManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ApiResponseDTO<Course>> createCourse(@RequestBody @Valid Create dto, Authentication authentication) {
        User admin = (User) authentication.getPrincipal();
        if (admin == null)
            throw new UserNotFoundException("Admin not found");
        Course course = courseManagementService.createCourse(dto.getName(),dto.getContent(),dto.getDepartment(), dto.getCredits(), admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.success("New course created successfully", course));
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<ApiResponseDTO<Course>> getCourseById(@PathVariable Long id){
        Course course = courseManagementService.getCourseById(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Course retrieved successfully", course));
    }

    //INCOMPLETE
    @GetMapping("/course/bulk")
    public ResponseEntity<ApiResponseDTO<List<Course>>> getAllCourses(){
        return ResponseEntity.ok(ApiResponseDTO.success("Courses list", null));
    }

    //INCOMPLETE
    @GetMapping("/course/{id}/students")
    public ResponseEntity<ApiResponseDTO<List<User>>> getAllStudentsInCourse(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponseDTO.success("Students List in course id: "+id, null));
    }

    @PatchMapping("/course/{id}")
    public ResponseEntity<ApiResponseDTO<Course>> updateCourseById(@PathVariable Long id, @RequestBody Create dto){
       Course course = courseManagementService.updateCourseById(id,dto);
        return ResponseEntity.ok(ApiResponseDTO.success("Course updated successfully", course));
    }

    // INCOMPLETE
    @DeleteMapping("/course/{id}")
    public ResponseEntity<ApiResponseDTO<Boolean>> deleteCourseById(@PathVariable Long id){
        boolean isDeleted = courseManagementService.deleteCourseById(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Course deleted successfully", isDeleted));
    }


}
