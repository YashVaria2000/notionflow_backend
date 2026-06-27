package com.yashvaria.notionflow.service;

import com.yashvaria.notionflow.dto.NoteDto;
import com.yashvaria.notionflow.entity.Note;
import com.yashvaria.notionflow.entity.User;
import com.yashvaria.notionflow.exception.ResourceNotFoundException;
import com.yashvaria.notionflow.respository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    private User getCurrentAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public List<NoteDto> getAllNotes() {
        User currentUser = getCurrentAuthenticatedUser();

        return noteRepository.findByUserId(currentUser.getId())
                .stream()
                .map(NoteDto::fromEntity)
                .toList();
    }

    public NoteDto getNoteById(Long id) {
        User currentUser = getCurrentAuthenticatedUser();

        Note note = noteRepository.findByIdAndUserId(id,  currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id " + id));

        return NoteDto.fromEntity(note);
    }

    public NoteDto createNote(NoteDto noteDto) {
        User currentUser = getCurrentAuthenticatedUser();
        Note note = noteDto.toEntity();
        note.setUser(currentUser);

        Date now = new Date();
        note.setCreatedAt(now);
        note.setUpdatedAt(now);

        return NoteDto.fromEntity(noteRepository.save(note));
    }

    public NoteDto updateNote(NoteDto noteDto) {
        User currentUser = getCurrentAuthenticatedUser();

        Note note = noteRepository.findByIdAndUserId(noteDto.getId(), currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id " + noteDto.getId()));

        note.setTitle(noteDto.getTitle());
        note.setContent(noteDto.getContent());
        note.setUpdatedAt(new Date());

        return NoteDto.fromEntity(noteRepository.save(note));
    }

    public void deleteNote(Long id) {
        User currentUser = getCurrentAuthenticatedUser();

        // Fetch the note securely
        Note note = noteRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cannot delete. Note not found with id: " + id));

        // Delete the entity object directly instead of using the ID
        noteRepository.delete(note);
    }
}
