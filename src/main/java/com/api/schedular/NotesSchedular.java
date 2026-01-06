package com.api.schedular;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.api.entity.Notes;
import com.api.repository.NotesRepository;

@Component
public class NotesSchedular {

	@Autowired
	NotesRepository notesRepository;
	
	@Scheduled(cron = "0 0 0 * * *")
	public void deletedNotesSchedular()
	{
		LocalDateTime cuttOfDays = LocalDateTime.now().minusDays(7);
	List<Notes> deletedNotes = 	notesRepository.isDeletedAndDeletedOnBefore(true , cuttOfDays);
	notesRepository.deleteAll(deletedNotes);
	}
}
