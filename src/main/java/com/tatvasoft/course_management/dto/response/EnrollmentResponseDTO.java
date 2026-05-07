package com.tatvasoft.course_management.dto.response;

import com.tatvasoft.course_management.entity.Enrollment;
import com.tatvasoft.course_management.enums.Department;
import com.tatvasoft.course_management.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponseDTO {
    public EnrollmentResponseDTO(Enrollment enrollment) {
        enrollmentId = enrollment.getId();
        courseId = enrollment.getCourse().getId();
        courseName = enrollment.getCourse().getName();
        courseContent = enrollment.getCourse().getContent();
        courseCredits = enrollment.getCourse().getCredits();
        courseDepartment = enrollment.getCourse().getDepartment();
        status = enrollment.getStatus();
        enrolledAt = enrollment.getEnrolledAt();
        completedAt = enrollment.getCompletedAt();
    }

    private Long enrollmentId;
    private Long courseId;
    private String courseName;
    private String courseContent;
    private Integer courseCredits;
    private Department courseDepartment;
    private Status status;
    private LocalDateTime enrolledAt;
    private LocalDateTime completedAt;
}
