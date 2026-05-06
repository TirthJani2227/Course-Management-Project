package com.tatvasoft.course_management.dto.request;

import com.tatvasoft.course_management.enums.Department;
import jakarta.validation.constraints.*;
import lombok.Data;

public class CourseManagementDTO {
    @Data
    public static class Create {
        @NotBlank(message = "Course name is required")
        @Size(min = 8, max = 250, message = "Course name must be between 8 and 250 characters")
        private String name;

        @NotBlank(message = "Course content is required")
        @Size(min = 10, max = 250, message = "Course content must be between 10 and 250 characters")
        private String content;

        @NotNull(message = "Credits are required")
        @Min(value = 1, message = "Course credits must be between 1 to 6")
        @Max(value = 6, message = "Course credits must be between 1 to 6")
        private Integer credits;

        @NotNull(message = "Department is required")
        private Department department;

        public void setName(String name) {
            this.name = (name != null) ? name.trim() : null;
        }

        public void setContent(String content) {
            this.content = (content != null) ? content.trim() : null;
        }


    }

}
