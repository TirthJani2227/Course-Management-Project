package com.tatvasoft.course_management.dto.response;

import com.tatvasoft.course_management.enums.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDTO {
    private Long id;
    private String name;
    private String content;
    private Integer credits;
    private Department department;
    private LocalDateTime createdAt;
}

