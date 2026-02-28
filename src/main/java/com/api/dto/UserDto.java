package com.api.dto;

import java.util.List;

import com.api.entity.Role;
import com.api.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder 
public class UserDto {
	private Integer id;
	private String firstName;
	private String lastName;
	private String email;
	private String mobNo;
	private String password;
	private List<RoleDto> role;
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@Setter
	@Builder
	public static class RoleDto{
		private Integer id;
		private String  name;
	}


}
