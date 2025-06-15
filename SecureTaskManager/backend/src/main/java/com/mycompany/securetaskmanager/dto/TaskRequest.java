package com.mycompany.securetaskmanager.dto;

public class TaskRequest {
    private String title;
    private String description;

    // Конструктор без параметров нужен для Jackson
    public TaskRequest() {}

    // getters and setters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
