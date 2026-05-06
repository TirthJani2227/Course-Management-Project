package com.tatvasoft.course_management.controller;

import com.tatvasoft.course_management.dto.response.ApiResponseDTO;
import com.tatvasoft.course_management.entity.Enrollment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class EnrollmentController {

    @PostMapping("/student/enroll")
    public ResponseEntity<ApiResponseDTO<Enrollment>> enrollStudent() {
        return null;
    }

}
