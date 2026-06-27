package com.yashvaria.notionflow.respository;

import com.yashvaria.notionflow.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByUserId(Long userId);

    // Ensure a user cannot view or edit someone else's note by guessing the note ID
    Optional<Note> findByIdAndUserId(Long id, Long userId);
}
