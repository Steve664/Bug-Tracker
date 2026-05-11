package com.bugtracker.dto;

import com.bugtracker.model.Severity;
import com.bugtracker.model.Status;
import java.time.LocalDateTime;

public class BugResponseDto {

    private Long id;
    private String title;
    private String description;
    private Status status;
    private Severity severity;
    private LocalDateTime createdAt;
    private Long userId;
    private String userName;
    private String userEmail;

    public BugResponseDto() {
    }

    public BugResponseDto(
        Long id,
        String title,
        String description,
        Status status,
        Severity severity,
        LocalDateTime createdAt,
        Long userId,
        String userName,
        String userEmail
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.severity = severity;
        this.createdAt = createdAt;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
