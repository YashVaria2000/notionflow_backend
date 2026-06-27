package com.yashvaria.notionflow.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TaskPriority {
    LOW,
    MEDIUM,
    HIGH;

    @JsonCreator
    public static TaskPriority fromString(String value) {
        if (value == null) return null;
        // Automatically converts incoming strings like "low" or "Low" to "LOW"
        return TaskPriority.valueOf(value.toUpperCase().trim());
    }
}
