package com.api.service;

import com.api.dto.LoginRequestDto;
import com.api.dto.LoginResponseDto;
import com.api.dto.UserDto;

public interface UserService {
	public Boolean register(UserDto userDto, String url) throws Exception;

	public LoginResponseDto login(LoginRequestDto loginRequestDto);

}
