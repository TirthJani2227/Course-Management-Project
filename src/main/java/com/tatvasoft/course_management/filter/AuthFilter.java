package com.tatvasoft.course_management.filter;

import com.tatvasoft.course_management.entity.User;
import com.tatvasoft.course_management.repository.UserRepository;
import com.tatvasoft.course_management.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerExceptionResolver;


@Component
public class AuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public AuthFilter(JwtService jwtService, UserRepository userRepository, HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String jwt = extractJwtFromCookie(request, "auth_token");
            if (jwt == null) {
                filterChain.doFilter(request, response);
                return;
            }

            final String userEmail;
            try {
                userEmail = jwtService.extractEmail(jwt);
            } catch (Exception e) {
                clearCookieAndSendError(response, "auth_token", HttpServletResponse.SC_UNAUTHORIZED, "Invalid or malformed JWT token");
                return;
            }
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                User user = userRepository.findByEmailAndIsDeletedFalse(userEmail).orElse(null);
                if (user == null) {
                    clearCookieAndSendError(response, "auth_token", HttpServletResponse.SC_UNAUTHORIZED, "User not found");
                    return;
                }

                if (!jwtService.isTokenValid(jwt, user)) {
                    clearCookieAndSendError(response, "auth_token", HttpServletResponse.SC_UNAUTHORIZED, "JWT token has expired. Please login again");
                    return;
                }

                // THis helps to set the user in the spring security context and we can access .hasRole("") after this
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }

    private String extractJwtFromCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if (cookieName.equals(cookie.getName())) {
                String value = cookie.getValue();
                return (value != null && !value.isBlank()) ? value : null;
            }
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        if (path.equals("/api/v1/auth/admin/register"))
            return false;

        return path.startsWith("/api/v1/auth/");
    }


    private void clearCookieAndSendError(HttpServletResponse response, String cookieName, int statusCode, String message) throws IOException {
        Cookie expiredCookie = new Cookie(cookieName, "");
        expiredCookie.setHttpOnly(true);
        expiredCookie.setPath("/");
        expiredCookie.setMaxAge(0);
        response.addCookie(expiredCookie);
        response.sendError(statusCode, message);
    }
}
