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
	public Boolean register(UserDto userDto, String url) throws Exception {

		validation.userValidation(userDto);
		User user = mapper.map(userDto, User.class);
		setRole(userDto, user);
<<<<<<< Updated upstream
=======

		AccountStatus accountStatus = AccountStatus.builder().isActive(false)
				.verificationCode(UUID.randomUUID().toString()).build();

		user.setAccountStatus(accountStatus);

>>>>>>> Stashed changes
		User saveUser = userRepo.save(user);
		if (!ObjectUtils.isEmpty(saveUser)) {
			// send email

			emailSend(saveUser);
			return true;
		}
		return false;
	}

<<<<<<< Updated upstream
	private  void emailSend(User saveUser) throws Exception {
		String message = "Hi, <b>" + saveUser.getFirstName() + "</b> <br> Your account Register successfully"
				+ "<br> Click the below Link and verify your account <br>" + "<a href = '#' Click Here </a> <br>"
				+ "Thanks , <br>Enotes.com"

		;
=======
	private void emailSend(User saveUser) throws Exception {
		String message = "Hi, <b> + [[username]]" + "</b> <br> Your account Register successfully"
				+ "<br> Click the below Link and verify your account <br>" + "<a href = '[[url]]'> Click Here </a> <br>"
				+ "Thanks , <br>Enotes.com";
		message = message.replace("[[username]]", saveUser.getFirstName());
		message = message.replace("[[url]]", "http://localhost:8080/api/v1/home/verify?uid=" + saveUser.getId()
				+ "&&code=" + saveUser.getAccountStatus().getVerificationCode());

>>>>>>> Stashed changes
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
