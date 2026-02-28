package com.api.controller;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.dto.LoginRequestDto;
import com.api.dto.LoginResponseDto;
import com.api.dto.UserDto;
import com.api.service.UserService;
import com.api.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/user")
public class AuthController {

	@Autowired
	private UserService userService;

	@PostMapping("/")
	public ResponseEntity<?> registerUser(@RequestBody UserDto userDto, HttpServletRequest request) throws Exception {
		String url = CommonUtil.getUrl(request);
		Boolean register = userService.register(userDto, url);
		if (register) {
			return CommonUtil.createBuildResponse("Registered Successfully", HttpStatus.CREATED);
		} else {
			return CommonUtil.createErrorResponseMessage("Registeration Failed", HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) throws Exception {


		LoginResponseDto loginResponseDto = userService.login(loginRequestDto);
		if (ObjectUtils.isEmpty(loginResponseDto)) {
			return CommonUtil.createErrorResponseMessage("invalid Creadentials", HttpStatus.BAD_REQUEST);
		}
		return CommonUtil.createBuildResponse(loginResponseDto, HttpStatus.OK);
	}
}
