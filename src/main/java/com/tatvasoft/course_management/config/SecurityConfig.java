package com.tatvasoft.course_management.config;

import com.tatvasoft.course_management.filter.AuthFilter;
import com.tatvasoft.course_management.service.UserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthFilter authFilter;

    public SecurityConfig(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");

                            response.getWriter().write("""
                                    {"success": false,"message": "Authentication Failed"}
                                    """);
                        })
                        .accessDeniedHandler(((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter().write("""
                                    {"success": false, "message": "Access Denied"}
                                    """);
                        }))
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/admin/register").hasRole("ADMIN")
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/student/**").hasRole("STUDENT")
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/course/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/course").authenticated()
                        .requestMatchers("/api/v1/course").hasRole("ADMIN")
                        .requestMatchers("/api/v1/course/**").hasRole("ADMIN")
                        .anyRequest().denyAll())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}