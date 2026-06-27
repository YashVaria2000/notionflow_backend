package com.yashvaria.notionflow.respository;

import com.yashvaria.notionflow.entity.Note;
import com.yashvaria.notionflow.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserId(Long userId);

    // Ensure a user cannot view or edit someone else's note by guessing the note ID
    Optional<Task> findByIdAndUserId(Long id, Long userId);
}
