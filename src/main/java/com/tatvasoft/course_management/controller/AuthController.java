package com.tatvasoft.course_management.controller;

import com.tatvasoft.course_management.dto.request.AuthRequestDTO;
import com.tatvasoft.course_management.dto.response.ApiResponseDTO;
import com.tatvasoft.course_management.entity.User;
import com.tatvasoft.course_management.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/student/login")
    public ResponseEntity<ApiResponseDTO<String>> loginStudent(@Valid @RequestBody AuthRequestDTO.Login dto, HttpServletResponse res) {
        String token = authService.loginStudent(dto.getEmail(), dto.getPassword());
        setTokenCookie(res, "auth_token", token, 86400, "/");
        return ResponseEntity.ok(ApiResponseDTO.success("Login successful", token));
    }

    @PostMapping("/student/register")
    public ResponseEntity<ApiResponseDTO<String>> registerStudent(@Valid @RequestBody AuthRequestDTO.Register dto, HttpServletResponse res) {
        String token = authService.registerStudent(dto.getEmail(), dto.getPassword(), dto.getName());
        setTokenCookie(res, "auth_token", token, 86400, "/");
        return ResponseEntity.ok(ApiResponseDTO.success("Registered successfully", token));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<ApiResponseDTO<String>> loginAdmin(@Valid @RequestBody AuthRequestDTO.Login dto, HttpServletResponse res) {
        String token = authService.loginAdmin(dto.getEmail(), dto.getPassword());
        setTokenCookie(res, "auth_token", token, 86400, "/");
        return ResponseEntity.ok(ApiResponseDTO.success("Login as Admin successfully", token));
    }

    @PostMapping("/admin/register")
    public ResponseEntity<ApiResponseDTO<String>> registerAdmin(@Valid @RequestBody AuthRequestDTO.Register dto, HttpServletResponse res, Authentication authentication) {
        User admin = (User) authentication.getPrincipal();
        String token = authService.registerAdmin(dto.getEmail(), dto.getPassword(), dto.getName(), admin);
        return ResponseEntity.ok(ApiResponseDTO.success("Registered Admin successfully", token));
    }


    private void setTokenCookie(HttpServletResponse response, String name,
                                String value, int maxAge, String path) {
        response.addHeader("Set-Cookie",
                name + "=" + value
                        + "; Max-Age=" + maxAge
                        + "; Path=" + path
                        + "; HttpOnly"
                        + "; SameSite=Strict");
    }
}
