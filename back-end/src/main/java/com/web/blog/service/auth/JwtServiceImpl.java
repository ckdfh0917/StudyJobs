package com.web.blog.service.auth;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtServiceImpl implements JwtService {
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expmin}")
	private Long expMin;
	
	@Override
	public String generateToken(final String email) {
		Claims claims = Jwts.claims().setSubject(email);
		Date now = new Date();
		return Jwts.builder()
				   .setHeaderParam("typ", "JWT")
				   .setClaims(claims)
				   .setIssuedAt(now)
				   .setExpiration(new Date(now.getTime() + expMin*60*1000L))
				   .signWith(SignatureAlgorithm.HS256, secret)
				   .compact();
	}
	
	@Override
	public String parseEmail(final String token) {
		return parseAllClaims(token).getSubject();
	}
	
	@Override
	public Claims parseAllClaims(final String token) throws ExpiredJwtException {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
	@Override
	public boolean isExpiredToken(final String token) {
		final Date expDate = parseAllClaims(token).getExpiration();
		return expDate.before(new Date());
	}
	
	@Override
	public boolean isValidToken(final String token, final String email) {
		return (!isExpiredToken(token) && parseEmail(token).equals(email));
	}
}
