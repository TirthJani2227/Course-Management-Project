package com.tatvasoft.course_management.service;

import com.tatvasoft.course_management.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public interface AuthService {
    String loginStudent(String email, String password);
    String registerStudent(String email, String password, String name);
    String loginAdmin(String email, String password);
    String registerAdmin(String email, String password, String name, User admin);
}
