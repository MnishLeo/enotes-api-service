 package com.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.entity.FileDetails;

public interface FileRepository extends JpaRepository<FileDetails, Integer>{

}
