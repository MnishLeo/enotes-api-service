package com.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {

	private UserDto userDto;
	private String token;
}
