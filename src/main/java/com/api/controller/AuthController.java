package com.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.dto.UserDto;
import com.api.service.UserService;
import com.api.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/user")
public class AuthController {

	@Autowired
	private UserService userService;

	@PostMapping("/")
	public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
		Boolean register = userService.register(userDto);
		if (register) {
			return CommonUtil.createBuildResponse("Registered Successfully", HttpStatus.CREATED);
		} else {
			return CommonUtil.createErrorResponseMessage("Registeration Failed", HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

}
