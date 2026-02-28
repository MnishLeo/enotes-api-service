package com.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.entity.User;

public interface userRepo extends JpaRepository<User, Integer> {

	Boolean existsByEmail(String email);

	User findByEmail(String email);

}
