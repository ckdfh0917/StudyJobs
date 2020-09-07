package com.web.blog.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

public interface JwtService {
	public String generateToken(final String email);
	public String parseEmail(final String token);
	public Claims parseAllClaims(final String token) throws ExpiredJwtException;
	public boolean isExpiredToken(final String token);
	public boolean isValidToken(final String token, final String email);
}
