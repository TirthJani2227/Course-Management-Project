package com.tatvasoft.course_management.dto.response;

import com.tatvasoft.course_management.entity.Course;
import com.tatvasoft.course_management.entity.Enrollment;
import com.tatvasoft.course_management.entity.User;
import com.tatvasoft.course_management.enums.Department;
import com.tatvasoft.course_management.enums.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class EnrolledCourseDetailsDTO {
    private Long courseId;
    private String courseName;
    private String courseContent;
    private Department department;
    private Integer credits;
    private List<StudentReponseDTO> studentsList;
    private Integer totalStudentsEnrolled = 0;

    public EnrolledCourseDetailsDTO(Course course, List<Enrollment> enrollments){
        courseId = course.getId();
        courseName = course.getName();
        courseContent = course.getContent();
        department = course.getDepartment();
        credits = course.getCredits();
        studentsList = enrollments.stream().map((enrollment)->{
            User currStudent = enrollment.getUser();
             return StudentReponseDTO.builder()
                     .name(currStudent.getName())
                     .email(currStudent.getEmail())
                     .status(enrollment.getStatus())
                     .enrolledAt(enrollment.getEnrolledAt())
                     .completedAt(enrollment.getCompletedAt())
                     .build();
        }).collect(Collectors.toList());
        totalStudentsEnrolled = studentsList.size();
    }

    @Builder
    @Data
    public static class StudentReponseDTO {
        private String name;
        private String email;
        private Status status;
        private LocalDateTime enrolledAt;
        private LocalDateTime completedAt;
    }

}

