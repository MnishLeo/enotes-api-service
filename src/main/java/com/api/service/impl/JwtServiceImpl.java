package com.api.service.impl;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.api.entity.User;
import com.api.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {

	private String secretKey = "";

	public JwtServiceImpl() {
		try {

			KeyGenerator generator = KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk = generator.generateKey();
			secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String generateToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", user.getId());
		claims.put("role", user.getRole());
		claims.put("status", user.getAccountStatus().getIsActive());
		String token = Jwts.builder().claims().add(claims).subject(user.getEmail())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 60 * 60 * 60 * 10)).and().signWith(getkey()).compact();
		return token;
	}

	private Key getkey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);

		return Keys.hmacShaKeyFor(keyBytes);

	}

	@Override
	public String extractUsername(String token) {
		Claims claims = extractAllClaims(token);
		return claims;
	}

	private Claims extractAllClaims(String token) {
		Claims claims = Jwts.parser().verifyWith(decrytKey(secretKey)).build().parseSignedClaims(token).getPayload();
		return claims;
	}

	private SecretKey decrytKey(String secretKey) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	@Override
	public Boolean validateToken(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		Boolean isExpired = isTokenExpired(token);
		if (username.equals(userDetails.getUsername()) && !isExpired) {
			return true;
		}
		return false;
	}

	private Boolean isTokenExpired(String token) {
		Claims claims = extractAllClaims(token);
		Date expiration = claims.getExpiration();
		return expiration.before(new Date());
	}

}