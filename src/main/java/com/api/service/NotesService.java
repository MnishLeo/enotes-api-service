package com.api.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.api.dto.NotesDto;
import com.api.entity.FileDetails;
import com.api.exception.ResourceNotFoundException;

public interface NotesService {

	public Boolean saveNotes(String notes, MultipartFile file) throws Exception;
	public List<NotesDto> getAllNotes();
	public byte[] downloadFile(FileDetails filedetails) throws  Exception;
	public FileDetails getFileDetails(Integer id) throws Exception;
}
