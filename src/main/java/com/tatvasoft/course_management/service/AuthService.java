package com.tatvasoft.course_management.service;

import com.tatvasoft.course_management.entity.User;

public interface AuthService {
    public User getUserEmailAndRole(String email,String role);
}
