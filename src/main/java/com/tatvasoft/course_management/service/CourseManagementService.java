package com.tatvasoft.course_management.service;

import com.tatvasoft.course_management.dto.request.CourseManagementDTO;
import com.tatvasoft.course_management.entity.Course;
import com.tatvasoft.course_management.entity.User;
import com.tatvasoft.course_management.enums.Department;

public interface CourseManagementService {
    Course createCourse(String name, String content, Department department, Integer credits, User admin);
    Course getCourseById(Long id);
    Course updateCourseById(Long id, CourseManagementDTO.Create dto);
    boolean deleteCourseById(Long id);
}
