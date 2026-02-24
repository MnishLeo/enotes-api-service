package com.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.exception.ResourceNotFoundException;
import com.api.service.HomeService;
import com.api.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/home/")
public class HomeController {

	@Autowired
	HomeService homeservice;

	@GetMapping("/verify")
	public ResponseEntity<?> verifyUserAccount(@RequestParam Integer uid , @RequestParam String code) throws ResourceNotFoundException
	{
		Boolean verifyAccount = homeservice.verifyAccount(uid, code);
		if(verifyAccount)
		return CommonUtil.createBuildResponse("Account verification Successfully", HttpStatus.OK);
		return CommonUtil.createErrorResponseMessage("Invalid Verification link", HttpStatus.BAD_REQUEST);
		
	}
}
