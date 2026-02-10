package com.api.util;

import com.api.repository.RoleRepo;
import com.api.repository.userRepo;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.api.dto.TodoDto;
import com.api.dto.TodoDto.StatusDto;
import com.api.dto.UserDto;
import com.api.entity.Role;
import com.api.enums.TodoStatus;
import com.api.exception.ResourceNotFoundException;

@Component
public class Validation {

	@Autowired
	private RoleRepo roleRepo;

	public void todoValidation(TodoDto todoDto) throws ResourceNotFoundException {
		StatusDto reqStatus = todoDto.getStatus();
		Boolean statusFound = false;
		for (TodoStatus st : TodoStatus.values()) {
			if (st.getId().equals(reqStatus.getId())) {
				statusFound = true;
			}
		}
		if (!statusFound) {
			throw new ResourceNotFoundException("invalid status");
		}
	}

	public void userValidation(UserDto userDto) {

		if (!StringUtils.hasText(userDto.getFirstName())) {
			throw new IllegalArgumentException("First Name is invalid");

		}
		if (!StringUtils.hasText(userDto.getLastName())) {
			throw new IllegalArgumentException("Last Name is invalid");

		}
		if (!StringUtils.hasText(userDto.getEmail()) || !userDto.getEmail().matches(Constants.EMAIL_REGEX)) {
			throw new IllegalArgumentException("Email is invalid");

		}
		if (!StringUtils.hasText(userDto.getMobNo()) || !userDto.getMobNo().matches(Constants.MOB_REGEX)) {
			throw new IllegalArgumentException("Mobile Number is invalid");

		}
		if (CollectionUtils.isEmpty(userDto.getRoles())) {
			throw new IllegalArgumentException("Role id is invalid");
		} else {
			List<Integer> roleIds = roleRepo.findAll().stream().map(r -> r.getId()).toList();
			List<Integer> invalidRoleIds = userDto.getRoles().stream().map(r -> r.getId())
					.filter(roleId -> roleIds.contains(roleId)).toList();
			if (CollectionUtils.isEmpty(invalidRoleIds)) {
				throw new IllegalArgumentException("Role id is invalid" + invalidRoleIds);
			}
		}
	}

}
