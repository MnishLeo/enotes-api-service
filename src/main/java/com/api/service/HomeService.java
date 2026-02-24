package com.api.service;

import com.api.exception.ResourceNotFoundException;

public interface HomeService {

	public Boolean verifyAccount(Integer userId , String verificationCode) throws ResourceNotFoundException;
}
