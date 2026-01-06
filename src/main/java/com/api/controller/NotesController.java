package com.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.dto.NoteResponse;
import com.api.dto.NotesDto;
import com.api.entity.FileDetails;
import com.api.exception.ResourceNotFoundException;
import com.api.service.NotesService;
import com.api.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/notes")
public class NotesController {
	@Autowired
	private NotesService notesService;

	@PostMapping("/")
	private ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file)
			throws Exception {
		Boolean saveNotes = notesService.saveNotes(notes, file);
		if (saveNotes) {
			return CommonUtil.createBuildResponse("notes saved successfully", HttpStatus.CREATED);
		}
		return CommonUtil.createErrorResponseMessage("notes did not saved", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("/download/{id}")
	public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception

	{
		FileDetails fileDtl =  notesService.getFileDetails(id);
		byte[] data = notesService.downloadFile(fileDtl);
		
		HttpHeaders headers = new HttpHeaders();
		String contentType = CommonUtil.getContentType(fileDtl.getOriginalFileName());
		headers.setContentType(MediaType.parseMediaType(contentType));
		headers.setContentDispositionFormData("attachment",fileDtl.getOriginalFileName());
		return ResponseEntity.ok().headers(headers).body(data);
	}

	@GetMapping("/")
	private ResponseEntity<?> getAllNotes() {
		List<NotesDto> notes = notesService.getAllNotes();
		if (CollectionUtils.isEmpty(notes)) {
			return ResponseEntity.noContent().build();
		}
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}
	@GetMapping("/user-notes")
	private ResponseEntity<?> getAllNotesByUser(
			@RequestParam(name= "pageNo",defaultValue = "0") Integer pageNo,
			@RequestParam(name="pageSize",defaultValue = "10") Integer pageSize   
			) {
	Integer userId =1;
		NoteResponse  notes = notesService.getAllNotesByUser(userId , pageNo , pageSize);
//		if (CollectionUtils.isEmpty(notes)) {
//			return ResponseEntity.noContent().build();
//		}
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}
	
	@GetMapping("/delete/{id}")
	public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws ResourceNotFoundException {
		notesService.softDelete(id);
		return CommonUtil.createBuildResponse("Note Delete Succesfully", HttpStatus.OK);
	}
	@GetMapping("/restore/{id}")
	public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws ResourceNotFoundException
	{
		notesService.restoreNotes(id);
		return CommonUtil.createBuildResponse("Note Restore Succesfully",HttpStatus.OK);
	}
	@GetMapping("/recycleBin")
	public ResponseEntity<?> getUserRecycleBinNotes () throws ResourceNotFoundException
	{
		Integer userId = 1;
		List<NotesDto> notes = notesService.getUserRecycleBinNotes(userId);
		if(CollectionUtils.isEmpty(notes))
		{
			return CommonUtil.createBuildResponse("Notes Not Available in Recycle Bin ", HttpStatus.OK);
		}
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws ResourceNotFoundException
	{
		notesService.hardDelete(id);
		return CommonUtil.createBuildResponse("Note Delete Succesfully",HttpStatus.OK);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> emptyRecycleBin () throws ResourceNotFoundException
	{
		int userId = 1;
		notesService.emptyRecycleBin(userId);
		return CommonUtil.createBuildResponse("Note Delete Succesfully",HttpStatus.OK);
	}
}

