package com.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.entity.Favourite_notes;

public interface FavouriteNoteRepo extends JpaRepository<Favourite_notes, Integer> {

	Optional<Favourite_notes> findByUserId(int userId);

}
