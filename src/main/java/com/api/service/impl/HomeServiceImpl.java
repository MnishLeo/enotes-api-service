package com.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.api.entity.AccountStatus;
import com.api.entity.User;
import com.api.exception.ResourceNotFoundException;
import com.api.exception.SuccessException;
import com.api.repository.userRepo;
import com.api.service.HomeService;

@Component
public class HomeServiceImpl implements HomeService {

	@Autowired
	private userRepo userRepo;

	@Override
	public Boolean verifyAccount(Integer userId, String verificationCode) throws ResourceNotFoundException {

		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("invalid userid"));
		if(user.getAccountStatus().getVerificationCode() == null)
		{
			throw new SuccessException("Account Already Verified");
		}
		
		
		if (user.getAccountStatus().getVerificationCode().equals(verificationCode)) {
			AccountStatus accountStatus = user.getAccountStatus();
			accountStatus.setIsActive(true);
			accountStatus.setVerificationCode(null);

			userRepo.save(user);

			return true;

		}

		return false;
	}



}
