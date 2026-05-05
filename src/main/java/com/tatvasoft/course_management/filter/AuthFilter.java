package com.tatvasoft.course_management.filter;

import com.tatvasoft.course_management.entity.User;
import com.tatvasoft.course_management.service.AuthService;
import com.tatvasoft.course_management.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final AuthService authService;

    @Autowired
    public AuthFilter(JwtService jwtService, AuthService authService){
        this.jwtService = jwtService;
        this.authService = authService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String jwt = extractJwtFromCookie(request, "auth_token");
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        final String userEmail;
        final String userRole;
        try {
            userEmail = jwtService.extractEmail(jwt);
            userRole = jwtService.extractClaim(jwt, (Map<String, String> map) -> {return map.getOrDefault("role", null)});
        } catch (Exception e) {
            clearCookieAndSendError(response, "auth_token", HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid or malformed JWT token");
            return;
        }
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            User user = authService.getUserEmailAndRole(userEmail, userRole);
            if (user == null) {
                clearCookieAndSendError(response, "auth_token", HttpServletResponse.SC_UNAUTHORIZED,
                        "Admin not found");
                return;
            }

            if (jwtService.isTokenValid(jwt, user)) {
                clearCookieAndSendError(response, "auth_token", HttpServletResponse.SC_UNAUTHORIZED,
                        "JWT token has expired. Please login again");
                return;
            }


            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(adminDetails, null,
                    List.of(new SimpleGrantedAuthority(userRole)));
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            request.setAttribute("loggedInUser", user);
        }

        filterChain.doFilter(request, response);


    }

    private String extractJwtFromCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null)
            return null;
        for (Cookie cookie : request.getCookies()) {
            if (cookieName.equals(cookie.getName())) {
                String value = cookie.getValue();
                return (value != null && !value.isBlank()) ? value : null;
            }
        }
        return null;
    }

    private void clearCookieAndSendError(HttpServletResponse response, String cookieName, int statusCode,
                                         String message) throws IOException {
        Cookie expiredCookie = new Cookie(cookieName, "");
        expiredCookie.setHttpOnly(true);
        expiredCookie.setPath("/");
        expiredCookie.setMaxAge(0);
        response.addCookie(expiredCookie);
        response.sendError(statusCode, message);
    }
}
