package com.yashvaria.notionflow.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    USER,
    ADMIN;

    @JsonCreator
    public static Role fromString(String value){
        if (value == null) return null;
        // Automatically converts incoming strings like "user" or "User" to "USER"
        return Role.valueOf(value.toUpperCase().trim());
    }
}
