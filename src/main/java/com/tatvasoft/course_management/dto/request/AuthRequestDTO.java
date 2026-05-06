package com.tatvasoft.course_management.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class AuthRequestDTO {
    @Data
    public static class Login {
        @Email
        @NotBlank
        private String email;
        @NotBlank
        @Size(min = 8, max = 250, message = "Password must be between 8 and 250 characters")
        private String password;

        public void setPassword(String password) {
            this.password = (password != null) ? password.trim() : null;
        }

        public void setEmail(String email) {
            this.email = (email != null) ? email.trim() : null;
        }
    }

    @Data
    public static class Register {
        @Email
        @NotBlank
        private String email;
        @NotBlank
        @Size(min = 2, max = 250, message = "Name must be between 2 and 250 characters")
        private String name;
        @NotBlank
        @Size(min = 8, max = 250, message = "Password must be between 8 and 250 characters")
        private String password;

        public void setPassword(String password) {
            this.password = (password != null) ? password.trim() : null;
        }

        public void setEmail(String email) {
            this.email = (email != null) ? email.trim() : null;
        }

        public void setName(String name) {
            this.name = (name != null) ? name.trim() : null;
        }
    }
}
