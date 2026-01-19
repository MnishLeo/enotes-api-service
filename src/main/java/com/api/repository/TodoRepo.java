package com.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.entity.Todo;

public interface TodoRepo extends JpaRepository<Todo, Integer>{

	List<Todo> findByCreatedBy(Integer userId);

}
