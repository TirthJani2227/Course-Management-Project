package com.tatvasoft.course_management.service.impl;

import com.tatvasoft.course_management.entity.User;
import com.tatvasoft.course_management.enums.Role;
import com.tatvasoft.course_management.exception.InvalidInputException;
import com.tatvasoft.course_management.exception.UserNotFoundException;
import com.tatvasoft.course_management.repository.UserRepository;
import com.tatvasoft.course_management.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidInputException("Email must not be blank");
        }

        return userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new UserNotFoundException(
                        "No active user found with email: " + email
                ));
    }
}
