package com.tatvasoft.course_management.service;

import java.util.function.Function;

import com.tatvasoft.course_management.entity.User;

import io.jsonwebtoken.Claims;

public interface JwtService {
	public String extractEmail(String token);

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

	public String generateToken(User user);

	public boolean isTokenValid(String token, User user);
}
