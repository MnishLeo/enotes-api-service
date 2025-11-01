package com.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

}
