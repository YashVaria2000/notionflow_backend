package com.yashvaria.notionflow.dto;

import com.yashvaria.notionflow.entity.Task;
import com.yashvaria.notionflow.entity.TaskPriority;
import com.yashvaria.notionflow.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private Long id;

    @NotBlank(message = "Title is mandatory and cannot be blank")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private Date createdAt;
    private Date updatedAt;

    public static TaskDto fromEntity(Task task){
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    public Task toEntity(){
        Task task = new Task();
        if(this.id != null) {
            task.setId(this.id);
        }
        task.setTitle(this.title);
        task.setDescription(this.description);
        task.setPriority(this.priority);
        task.setStatus(this.status);
        return task;
    }
}
