package com.api.service;

import java.util.List;

import com.api.dto.NotesDto;

public interface NotesService {

	public Boolean saveNotes(NotesDto notesDto) throws Exception;
	public List<NotesDto> getAllNotes();
}
