package com.api.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.api.dto.NotesDto;

public interface NotesService {

	public Boolean saveNotes(String notes, MultipartFile file) throws Exception;
	public List<NotesDto> getAllNotes();
}
