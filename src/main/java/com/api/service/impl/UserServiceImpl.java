package com.api.service.impl;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.api.config.security.CustomUserDetails;
import com.api.dto.EmailRequest;
import com.api.dto.LoginRequestDto;
import com.api.dto.LoginResponseDto;
import com.api.dto.UserDto;
import com.api.entity.AccountStatus;
import com.api.entity.Role;
import com.api.entity.User;
import com.api.repository.CategoryRepository;
import com.api.repository.RoleRepo;
import com.api.repository.userRepo;
import com.api.service.JwtService;
import com.api.service.UserService;
import com.api.util.Validation;

@Service
public class UserServiceImpl implements UserService {

	private final CategoryRepository categoryRepository;

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

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private BCryptPasswordEncoder encodePassord;

	@Autowired
	private JwtService jwtService;

	UserServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public Boolean register(UserDto userDto, String url) throws Exception {

		validation.userValidation(userDto);
		User user = mapper.map(userDto, User.class);
		setRole(userDto, user);

		AccountStatus accountStatus = AccountStatus.builder().isActive(false)
				.verificationCode(UUID.randomUUID().toString()).build();

		user.setAccountStatus(accountStatus);
		user.setPassword(encodePassord.encode(userDto.getPassword()));

		User saveUser = userRepo.save(user);
		if (!ObjectUtils.isEmpty(saveUser)) {
			// send email

			emailSend(saveUser, url);
			return true;
		}
		return false;
	}

	private void emailSend(User saveUser, String url) throws Exception {
		String message = "Hi, <b> + [[username]]" + "</b> <br> Your account Register successfully"
				+ "<br> Click the below Link and verify your account <br>" + "<a href = '[[url]]'> Click Here </a> <br>"
				+ "Thanks , <br>Enotes.com";
		message = message.replace("[[username]]", saveUser.getFirstName());
		message = message.replace("[[url]]", url + "/api/v1/home/verify?uid=" + saveUser.getId() + "&&code="
				+ saveUser.getAccountStatus().getVerificationCode());

		EmailRequest emailRequest = EmailRequest.builder().to(saveUser.getEmail())
				.title("Account Creation confirmation").subject("Account Created Success").msg(message).

				build();
		emailService.send(emailRequest);

	}

	private void setRole(UserDto userDto, User user) {
		List<Integer> reqRoleId = userDto.getRole().stream().map(r -> r.getId()).toList();
		List<Role> roles = roleRepo.findAllById(reqRoleId);
		user.setRole(roles);
	}

	@Override
	public LoginResponseDto login(LoginRequestDto loginRequestDto) {
		Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
		if (authenticate.isAuthenticated()) {
			CustomUserDetails cUserDetails = (CustomUserDetails) authenticate.getPrincipal();
			String token = jwtService.generateToken(cUserDetails.getUser());
			LoginResponseDto loginResponseDto = LoginResponseDto.builder().token(token)
					.userDto(mapper.map(cUserDetails.getUser(), UserDto.class)).

					build();
			return loginResponseDto;
		}

		return null;
	}

}
