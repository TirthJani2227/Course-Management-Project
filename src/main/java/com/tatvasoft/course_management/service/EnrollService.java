package com.tatvasoft.course_management.service;

import com.tatvasoft.course_management.dto.response.EnrolledCourseDetailsDTO;
import com.tatvasoft.course_management.dto.response.EnrollmentResponseDTO;
import com.tatvasoft.course_management.dto.response.MyProfileResponseDTO;
import com.tatvasoft.course_management.entity.User;

public interface EnrollService {
    EnrollmentResponseDTO enrollStudent(User student, Long courseId);
    void markCourseCompleted(User student, Long courseId);
    MyProfileResponseDTO getStudentProfile(User student);
    EnrolledCourseDetailsDTO getStudentsEnrolledInCourse(Long courseId);
}
