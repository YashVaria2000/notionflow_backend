package com.yashvaria.notionflow.service;

import com.yashvaria.notionflow.dto.TaskDto;
import com.yashvaria.notionflow.entity.Note;
import com.yashvaria.notionflow.entity.Task;
import com.yashvaria.notionflow.entity.User;
import com.yashvaria.notionflow.exception.ResourceNotFoundException;
import com.yashvaria.notionflow.respository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    private User getCurrentAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public List<TaskDto> getAllTasks() {
        User currentUser = getCurrentAuthenticatedUser();
        return taskRepository.findByUserId(currentUser.getId())
                .stream()
                .map(TaskDto::fromEntity)
                .toList();
    }

    public TaskDto getTaskById(Long id) {
        User currentUser = getCurrentAuthenticatedUser();

        Task task = taskRepository.findByIdAndUserId(id,  currentUser.getId())
                .orElseThrow(()-> new ResourceNotFoundException("Task not found with id "+ id));

        return TaskDto.fromEntity(task);
    }

    public TaskDto createTask(TaskDto taskDto) {
        User currentUser = getCurrentAuthenticatedUser();

        Task task = taskDto.toEntity();
        task.setUser(currentUser);

        task.setCreatedAt(new Date());
        task.setUpdatedAt(new Date());

        return TaskDto.fromEntity(taskRepository.save(task));
    }

    public TaskDto updateTask(TaskDto taskDto) {
        User currentUser = getCurrentAuthenticatedUser();

        Task task = taskRepository.findByIdAndUserId(taskDto.getId(),  currentUser.getId())
                .orElseThrow(()-> new ResourceNotFoundException("Task not found with id "+ taskDto.getId()));

        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setPriority(taskDto.getPriority());
        task.setStatus(taskDto.getStatus());
        task.setUpdatedAt(new Date());

        return TaskDto.fromEntity(taskRepository.save(task));
    }

    public void deleteTask(Long id) {
        User currentUser = getCurrentAuthenticatedUser();

        Task task = taskRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cannot delete. Note not found with id: " + id));

        taskRepository.delete(task);
    }
}
