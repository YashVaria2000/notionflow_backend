package com.yashvaria.notionflow.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TaskStatus{
    TODO,
    IN_PROGRESS,
    DONE;

    @JsonCreator
    public static TaskStatus fromString(String value) {
        if (value == null) return null;
        // Automatically converts incoming strings like "todo" or "ToDo" to "TODO"
        return TaskStatus.valueOf(value.toUpperCase().trim());
    }
}
