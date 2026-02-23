package com.api.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.api.dto.EmailRequest;
import com.api.dto.UserDto;
import com.api.entity.Role;
import com.api.entity.User;
import com.api.repository.RoleRepo;
import com.api.repository.userRepo;
import com.api.service.UserService;

import com.api.util.Validation;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private RoleRepo roleRepo;
	@Autowired
	private userRepo userRepo;

	@Autowired
	private Validation validation;

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private EmailService emailService;

	@Override
	public Boolean register(UserDto userDto) throws Exception {

		validation.userValidation(userDto);
		User user = mapper.map(userDto, User.class);
		setRole(userDto, user);
		User saveUser = userRepo.save(user);
		if (!ObjectUtils.isEmpty(saveUser)) {
			// send email

			emailSend(saveUser);
			return true;
		}
		return false;
	}

	private  void emailSend(User saveUser) throws Exception {
		String message = "Hi, <b>" + saveUser.getFirstName() + "</b> <br> Your account Register successfully"
				+ "<br> Click the below Link and verify your account <br>" + "<a href = '#' Click Here </a> <br>"
				+ "Thanks , <br>Enotes.com"

		;
		EmailRequest emailRequest = EmailRequest.builder().to(saveUser.getEmail())
				.title("Account Creation confirmation").subject("Account Created Success").msg(message).

				build();
		emailService.send(emailRequest);

	}

	private void setRole(UserDto userDto, User user) {
		List<Integer> reqRoleId = userDto.getRoles().stream().map(r -> r.getId()).toList();
		List<Role> roles = roleRepo.findAllById(reqRoleId);
		user.setRole(roles);
	}

}
