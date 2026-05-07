package com.tatvasoft.course_management.service;

import com.tatvasoft.course_management.dto.request.CourseManagementDTO;
import com.tatvasoft.course_management.dto.response.CourseResponseDTO;
import com.tatvasoft.course_management.entity.Course;
import com.tatvasoft.course_management.entity.User;
import com.tatvasoft.course_management.enums.Department;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseManagementService {
    CourseResponseDTO createCourse(String name, String content, Department department, Integer credits, User admin);

    CourseResponseDTO getCourseById(Long id);

    CourseResponseDTO updateCourseById(Long id, CourseManagementDTO.Create dto);

    boolean deleteCourseById(Long id);

    Page<CourseResponseDTO> getAllCourses(String name, Department department, Pageable pageable);
}
