package com.bugtracker.dto;

import com.bugtracker.model.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BugCreateRequestDto {

    @NotBlank(message = "Title is required.")
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters.")
    private String title;

    @NotBlank(message = "Description is required.")
    @Size(min = 10, max = 2000, message = "Description must be between 10 and 2000 characters.")
    private String description;

    @NotNull(message = "Severity is required.")
    private Severity severity;

    @NotNull(message = "User id is required.")
    private Long userId;

    public BugCreateRequestDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
