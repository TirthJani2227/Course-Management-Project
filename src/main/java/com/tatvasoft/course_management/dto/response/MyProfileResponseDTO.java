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
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Data
public class MyProfileResponseDTO {
    private List<CourseEnrolled> coursesEnrolled;
    private int totalCredits = 0;
    private int totalCoursesEnrolled = 0;
    private int totalCompletedCourses = 0;
    private String name;
    private String email;

    public MyProfileResponseDTO(List<Enrollment> enrollments, User student) {
        name = student.getName();
        email = student.getEmail();
        totalCoursesEnrolled = enrollments.size();
        coursesEnrolled = enrollments.stream().map((
                enrollment -> {
                    Course currCourse = enrollment.getCourse();
                    if(enrollment.getStatus() == Status.COMPLETED){
                        totalCompletedCourses++;
                        totalCredits+=currCourse.getCredits();
                    }
                    return CourseEnrolled.builder()
                            .courseId(currCourse.getId())
                            .courseName(currCourse.getName())
                            .courseContent(currCourse.getContent())
                            .courseCredits(currCourse.getCredits())
                            .department(currCourse.getDepartment())
                            .enrolledAt(enrollment.getEnrolledAt())
                            .courseStatus(enrollment.getStatus()).build();
                })).collect(Collectors.toList());
    }

    @Builder
    @Data
    public static class CourseEnrolled {
        private Long courseId;
        private String courseName;
        private String courseContent;
        private Integer courseCredits;
        private Department department;
        private LocalDateTime enrolledAt;
        private Status courseStatus;
    }

}

