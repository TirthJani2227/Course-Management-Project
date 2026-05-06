package com.tatvasoft.course_management.service.impl;

import com.tatvasoft.course_management.entity.User;
import com.tatvasoft.course_management.enums.Role;
import com.tatvasoft.course_management.exception.InvalidCredentialException;
import com.tatvasoft.course_management.exception.InvalidInputException;
import com.tatvasoft.course_management.repository.UserRepository;
import com.tatvasoft.course_management.service.AuthService;
import com.tatvasoft.course_management.service.JwtService;
import com.tatvasoft.course_management.util.PasswordUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository, JwtService jwtService) {
        this.repository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public String loginStudent(String email, String password) {
        if (email == null || password == null)
            throw new InvalidInputException("Invalid Input");

        User user = repository.findByEmailAndIsDeletedFalse(email).orElse(null);

        if (user == null)
            throw new InvalidInputException("Invalid Credentials!");

        if (!PasswordUtil.verifyPassword(password, user.getPassword()))
            throw new InvalidInputException("Invalid Credentials!");

        return jwtService.generateToken(user);

    }

    @Override
    public String registerStudent(String email, String password, String name) {
        if (email == null || password == null || name == null)
            throw new InvalidInputException("Invalid Input");

        User user = repository.findByEmailAndIsDeletedFalse(email).orElse(null);

        if (user != null)
            throw new InvalidCredentialException("User Already Exists");

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setRole(Role.STUDENT);
        newUser.setName(name);
        String hashedPassword = PasswordUtil.hashPassword(password);
        newUser.setPassword(hashedPassword);

        repository.save(newUser);

        return jwtService.generateToken(newUser);
    }

    @Override
    public String registerAdmin(String email, String password, String name, User admin) {
        if (email == null || password == null || name == null)
            throw new InvalidInputException("Invalid Input");

        User existingAdmin = repository.findByEmailAndIsDeletedFalse(email).orElse(null);

        if (existingAdmin != null)
            throw new InvalidCredentialException("User Already Exists");

        User newAdmin = new User();
        newAdmin.setEmail(email);
        newAdmin.setRole(Role.STUDENT);
        newAdmin.setName(name);
        String hashedPassword = PasswordUtil.hashPassword(password);
        newAdmin.setPassword(hashedPassword);
        newAdmin.setCreatedBy(admin);
        repository.save(newAdmin);
        return jwtService.generateToken(admin);
    }

    @Override
    public String loginAdmin(String email, String password) {
        if (email == null || password == null)
            throw new InvalidInputException("Invalid Input");

        User admin = repository.findByEmailAndIsDeletedFalse(email).orElse(null);

        if (admin == null)
            throw new InvalidInputException("Invalid Credentials!");

        if (!admin.getRole().toString().equals("ADMIN"))
            throw new InvalidInputException("Invalid Credentials!");

        if (!PasswordUtil.verifyPassword(password, admin.getPassword()))
            throw new InvalidInputException("Invalid Credentials!");

        return jwtService.generateToken(admin);
    }
}
