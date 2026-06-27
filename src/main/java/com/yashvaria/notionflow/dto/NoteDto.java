package com.yashvaria.notionflow.dto;

import com.yashvaria.notionflow.entity.Note;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteDto {
    private Long id;

    @NotBlank(message = "Title is mandatory and cannot be blank")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;
    private String content;
    private Date createdAt;
    private Date updatedAt;

    public static NoteDto fromEntity(Note note) {
        return new NoteDto(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getCreatedAt(),
                note.getUpdatedAt()
            );
    }

    public Note toEntity() {
        Note note = new Note();
        if(this.id != null) {
            note.setId(this.id);
        }
        note.setTitle(title);
        note.setContent(content);

        return note;
    }

    @Override
    public String toString() {
        return "NoteDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
