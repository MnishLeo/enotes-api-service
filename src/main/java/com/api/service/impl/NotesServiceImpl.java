package com.api.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import com.api.controller.NotesController;
import com.api.dto.FavouriteNoteDto;
import com.api.dto.NoteResponse;
import com.api.dto.NotesDto;
import com.api.dto.NotesDto.CategoryDto;
import com.api.dto.NotesDto.FileDto;
import com.api.entity.Favourite_notes;
import com.api.entity.FileDetails;
import com.api.entity.Notes;
import com.api.exception.ResourceNotFoundException;
import com.api.repository.CategoryRepository;
import com.api.repository.FavouriteNoteRepo;
import com.api.repository.FileRepository;
import com.api.repository.NotesRepository;
import com.api.service.NotesService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NotesServiceImpl implements NotesService {

	// private final NotesController notesController;

	@Autowired
	private NotesRepository notesRepository;

	@Autowired
	private FavouriteNoteRepo favouriteNoteRepo;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private CategoryRepository categoryRepository;

	@Value("${file.upload.path}")
	private String uploadPath;

	@Autowired
	private FileRepository fileRepository;

//	NotesServiceImpl(NotesController notesController) {
//		this.notesController = notesController;
//	}

	@Override
	public Boolean saveNotes(String notes, MultipartFile file) throws Exception {
		ObjectMapper obj = new ObjectMapper();
		NotesDto noteDto = obj.readValue(notes, NotesDto.class);
		noteDto.setIsDeleted(false);
		noteDto.setDeletedOn(null);

		checkCategoryExist(noteDto.getCategory());

		if (ObjectUtils.isEmpty(noteDto.getId())) {
			updateNotes(noteDto, file);

		}

		Notes noteMap = mapper.map(noteDto, Notes.class);

		FileDetails details = saveFileDetails(file);
		if (!ObjectUtils.isEmpty(details)) {
			noteMap.setFileDetails(details);
		} else {
			if (ObjectUtils.isEmpty(noteDto.getId())) {
				// noteMap.setFileDetails(null);
			}

		}
		Notes saveNotes = notesRepository.save(noteMap);
		if (!ObjectUtils.isEmpty(saveNotes)) {
			return true;
		}
		return false;
	}

	private void updateNotes(NotesDto noteDto, MultipartFile file) throws ResourceNotFoundException {
		Notes existNotes = notesRepository.findById(noteDto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Invalid Notes id"));

		if (ObjectUtils.isEmpty(file)) {
			noteDto.setFileDetails(mapper.map(existNotes.getFileDetails(), FileDto.class));
		}
	}

	private FileDetails saveFileDetails(MultipartFile file) throws IOException {
		if (!ObjectUtils.isEmpty(file) && !file.isEmpty()) {

			String originalFileName = file.getOriginalFilename();
			String extention = FilenameUtils.getExtension(file.getOriginalFilename());

			List<String> extentionAllow = Arrays.asList("pdf", "xlsx", "jpg", "png");
			if (!extentionAllow.contains(extention)) {
				throw new IllegalArgumentException("invalid file Format ! upload only .pdf , xlsx , jpg");
			}

			String rndNumber = UUID.randomUUID().toString();

			String uploadfileName = rndNumber + "." + extention;

			File savefile = new File(uploadPath);
			if (!savefile.exists()) {
				savefile.mkdir();

			}
			String storePath = uploadPath.concat(uploadfileName);

			long upload = Files.copy(file.getInputStream(), Paths.get(storePath));
			if (upload != 0) {
				FileDetails fileDetails = new FileDetails();
				fileDetails.setDisplayFileName(getDisplayName(originalFileName));
				fileDetails.setOriginalFileName(originalFileName);
				fileDetails.setUploadFileName(uploadfileName);
				fileDetails.setPath(storePath);
				fileDetails.setFileSize(file.getSize());
				FileDetails saveFileDetails = fileRepository.save(fileDetails);
				return saveFileDetails;
			}

		}

		return null;
	}

	private String getDisplayName(String originalFilename) {
		String extention = FilenameUtils.getExtension(originalFilename);
		String fileName = FilenameUtils.getName(originalFilename);
		if (fileName.length() > 8) {
			fileName = fileName.substring(0, 7);
		}
		fileName = fileName + "." + extention;
		return fileName;
	}

	private void checkCategoryExist(CategoryDto category) throws ResourceNotFoundException {
		categoryRepository.findById(category.getId())
				.orElseThrow(() -> new ResourceNotFoundException("category id is invalid"));

	}

	@Override
	public List<NotesDto> getAllNotes() {

		return notesRepository.findAll().stream().map(note -> mapper.map(note, NotesDto.class)).toList();

	}

	@Override
	public byte[] downloadFile(FileDetails fileDetails) throws Exception {

		InputStream io = new FileInputStream(fileDetails.getPath());

		return StreamUtils.copyToByteArray(io);
	}

	@Override
	public FileDetails getFileDetails(Integer id) throws Exception {
		FileDetails fileDtls = fileRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("File is not avaiable"));

		return fileDtls;
	}

	@Override
	public NoteResponse getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize) {

		Pageable pageable = PageRequest.of(pageNo, pageSize);

		Page<Notes> pageNotes = notesRepository.findByCreatedByAndIsDeletedFalse(userId, pageable);
		List<NotesDto> notesDto = pageNotes.get().map(n -> mapper.map(n, NotesDto.class)).toList();
		NoteResponse noteResponse = NoteResponse.builder().notes(notesDto).pageNo(pageNotes.getNumber())
				.pageSize(pageNotes.getSize()).totalElement(pageNotes.getTotalElements())
				.totalPages(pageNotes.getTotalPages()).isFirst(pageNotes.isFirst()).isLast(pageNotes.isLast())

				.build();
		return noteResponse;
	}

	@Override
	public void softDelete(Integer id) throws ResourceNotFoundException {
		Notes notes = notesRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Invalid id Or id not found in Db"));
		notes.setIsDeleted(true);
		notes.setDeletedOn(LocalDateTime.now());
		notesRepository.save(notes);
	}

	@Override
	public void restoreNotes(Integer id) throws ResourceNotFoundException {
		Notes notes = notesRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Invalid id Or id not found in Db"));
		notes.setIsDeleted(false);
		notes.setDeletedOn(null);
		notesRepository.save(notes);

	}

	@Override
	public List<NotesDto> getUserRecycleBinNotes(Integer userId) {
		List<Notes> recycleNotes = notesRepository.findByCreatedByAndIsDeletedTrue(userId);
		List<NotesDto> notesDtoList = recycleNotes.stream().map(note -> mapper.map(note, NotesDto.class)).toList();

		return notesDtoList;
	}

	@Override
	public void hardDelete(Integer id) throws ResourceNotFoundException {
		Notes notes = notesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notes not found"));
		if (notes.getIsDeleted()) {
			notesRepository.delete(notes);

		} else {
			throw new IllegalArgumentException("Sorry You cant hard delete directly");
		}

	}

	@Override
	public void emptyRecycleBin(int userId) {
		List<Notes> recycleNotes = notesRepository.findByCreatedByAndIsDeletedTrue(userId);
		if (!CollectionUtils.isEmpty(recycleNotes)) {
			notesRepository.deleteAll(recycleNotes);
		}

	}

	@Override
	public void favouriteNote(Integer noteId) throws ResourceNotFoundException {
		int userId = 1;
		Notes notes = notesRepository.findById(noteId)
				.orElseThrow(() -> new ResourceNotFoundException("Note id invalid or note not found"));
		Favourite_notes favourite_notes = Favourite_notes.builder().notes(notes).userId(userId).build();
		favouriteNoteRepo.save(favourite_notes);

	}

	@Override
	public void unfavouriteNote(Integer favouriteNoteId) throws ResourceNotFoundException {
		Favourite_notes favNotes = favouriteNoteRepo.findById(favouriteNoteId)
				.orElseThrow(() -> new ResourceNotFoundException("Favourite Note id invalid or note not found"));
		favouriteNoteRepo.delete(favNotes);

	}

	@Override
	public List<FavouriteNoteDto> getUserFavouriteNote() throws ResourceNotFoundException {
		int userId = 1;

		favouriteNoteRepo.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Favourite Note id invalid or note not found"));

		return null;
	}

}
