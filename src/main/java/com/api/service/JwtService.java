package com.api.service;

import com.api.entity.User;

public interface JwtService {
	public String generateToken(User user);

}
