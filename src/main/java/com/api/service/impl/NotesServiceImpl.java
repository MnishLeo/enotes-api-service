package com.api.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.api.dto.NotesDto;
import com.api.dto.NotesDto.CategoryDto;
import com.api.entity.Notes;
import com.api.exception.ResourceNotFoundException;
import com.api.repository.CategoryRepository;
import com.api.repository.NotesRepository;
import com.api.service.NotesService;

@Service
public class NotesServiceImpl implements NotesService {

	@Autowired
	private NotesRepository notesRepository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Boolean saveNotes(NotesDto notesDto) throws Exception {

		checkCategoryExist(notesDto.getCategory());
		Notes note = mapper.map(notesDto, Notes.class);
		Notes saveNotes = notesRepository.save(note);
		if (!ObjectUtils.isEmpty(saveNotes)) {
			return true;
		}
		return false;
	}

	private void checkCategoryExist(CategoryDto category) throws ResourceNotFoundException {
		categoryRepository.findById(category.getId())
				.orElseThrow(() -> new ResourceNotFoundException("category id is invalid"));

	}

	@Override
	public List<NotesDto> getAllNotes() {

		return notesRepository.findAll().stream().map(note -> mapper.map(note, NotesDto.class)).toList();

	}

}
