package com.tatvasoft.course_management.controller;

import com.tatvasoft.course_management.dto.response.ApiResponseDTO;
import com.tatvasoft.course_management.dto.response.EnrolledCourseDetailsDTO;
import com.tatvasoft.course_management.dto.response.EnrollmentResponseDTO;
import com.tatvasoft.course_management.dto.response.MyProfileResponseDTO;
import com.tatvasoft.course_management.entity.Enrollment;
import com.tatvasoft.course_management.entity.User;
import com.tatvasoft.course_management.exception.UserNotFoundException;
import com.tatvasoft.course_management.service.EnrollService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EnrollmentController {

    private final EnrollService service;

    @GetMapping("/admin/course/{courseId}/students")
    public ResponseEntity<ApiResponseDTO<EnrolledCourseDetailsDTO>> studentsEnrolledInCouse(@PathVariable Long courseId) {
        EnrolledCourseDetailsDTO result = service.getStudentsEnrolledInCourse(courseId);
        return ResponseEntity.ok(ApiResponseDTO.success("Fetched course details and list of students enrolled in course: " + result.getCourseName(), result));
    }

    @PostMapping("/student/enroll/{courseId}")
    public ResponseEntity<ApiResponseDTO<EnrollmentResponseDTO>> enrollStudent(@PathVariable Long courseId, Authentication authentication) {
        User student = (User) authentication.getPrincipal();
        if (student == null) throw new UserNotFoundException("No student found to enroll");

        EnrollmentResponseDTO enrollment = service.enrollStudent(student, courseId);
        return ResponseEntity.ok(ApiResponseDTO.success("Student enrolled successfully in course: " + enrollment.getCourseName(), enrollment));
    }

    @PatchMapping("/student/complete/{courseId}")
    public ResponseEntity<ApiResponseDTO<Void>> completeCourse(@PathVariable Long courseId, Authentication authentication) {
        User student = (User) authentication.getPrincipal();
        if (student == null) throw new UserNotFoundException("No such student found");

        service.markCourseCompleted(student, courseId);
        return ResponseEntity.ok(ApiResponseDTO.success("Student completed course successfully"));
    }

    @GetMapping("/student/my-profile")
    public ResponseEntity<ApiResponseDTO<MyProfileResponseDTO>> myProfile(Authentication authentication) {
        User student = (User) authentication.getPrincipal();
        if (student == null) throw new UserNotFoundException("No such student found");

        MyProfileResponseDTO profile = service.getStudentProfile(student);
        return ResponseEntity.ok(ApiResponseDTO.success("Student profile fetched", profile));
    }

    @GetMapping("/student/my-courses")
    public ResponseEntity<ApiResponseDTO<List<MyProfileResponseDTO.CourseEnrolled>>> myCourses(Authentication authentication) {
        User student = (User) authentication.getPrincipal();
        if (student == null) throw new UserNotFoundException("No such student found");

        List<MyProfileResponseDTO.CourseEnrolled> courses = service.getStudentProfile(student).getCoursesEnrolled();
        return ResponseEntity.ok(ApiResponseDTO.success("Student profile fetched", courses));
    }
}
