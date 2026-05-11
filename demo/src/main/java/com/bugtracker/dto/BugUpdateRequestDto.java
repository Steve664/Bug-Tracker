package com.bugtracker.dto;

import com.bugtracker.model.Status;
import jakarta.validation.constraints.NotNull;

public class BugUpdateRequestDto {

    @NotNull(message = "Status is required.")
    private Status status;

    public BugUpdateRequestDto() {
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
