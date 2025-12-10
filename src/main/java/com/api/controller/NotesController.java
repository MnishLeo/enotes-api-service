package com.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.dto.NotesDto;
import com.api.service.NotesService;
import com.api.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/notes")
public class NotesController {
	@Autowired
	private NotesService notesService;

	@PostMapping("/")
	private ResponseEntity<?> saveNotes(@RequestBody NotesDto notesDto) throws Exception {
		Boolean saveNotes = notesService.saveNotes(notesDto);
		if (saveNotes) {
			return CommonUtil.createBuildResponse("notes saved successfully", HttpStatus.CREATED);
		}
		return CommonUtil.createErrorResponseMessage("notes did not saved", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@GetMapping("/")
	private ResponseEntity<?> getAllNotes() {
		List<NotesDto> notes = notesService.getAllNotes();
		if (CollectionUtils.isEmpty(notes)) {
			return ResponseEntity.noContent().build();
		}
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}
}
