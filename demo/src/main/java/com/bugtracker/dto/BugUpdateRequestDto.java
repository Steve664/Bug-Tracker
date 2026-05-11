package com.bugtracker.dto;

import com.bugtracker.model.Status;

public class BugUpdateRequestDto {

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
