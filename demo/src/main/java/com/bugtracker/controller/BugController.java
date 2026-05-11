package com.bugtracker.controller;

import com.bugtracker.dto.BugCreateRequestDto;
import com.bugtracker.dto.BugResponseDto;
import com.bugtracker.dto.BugUpdateRequestDto;
import com.bugtracker.model.Bug;
import com.bugtracker.service.BugService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bugs")
public class BugController {

    private final BugService bugService;

    public BugController(BugService bugService) {
        this.bugService = bugService;
    }

    @PostMapping("/create")
    public ResponseEntity<BugResponseDto> createBug(@Valid @RequestBody BugCreateRequestDto request) {
        Bug createdBug = bugService.createBug(
            request.getTitle(),
            request.getDescription(),
            request.getSeverity(),
            request.getUserId()
        );

        return ResponseEntity
            .created(URI.create("/api/bugs/" + createdBug.getId()))
            .body(toResponseDto(createdBug));
    }

    @GetMapping("/all")
    public ResponseEntity<List<BugResponseDto>> getAllBugs() {
        List<BugResponseDto> bugs = bugService.getAllBugs()
            .stream()
            .map(this::toResponseDto)
            .toList();

        return ResponseEntity.ok(bugs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BugResponseDto> getBugById(@PathVariable Long id) {
        Bug bug = bugService.getBugById(id);
        return ResponseEntity.ok(toResponseDto(bug));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<BugResponseDto> updateBugStatus(
        @PathVariable Long id,
        @Valid @RequestBody BugUpdateRequestDto request
    ) {
        Bug updatedBug = bugService.updateBugStatus(id, request.getStatus());
        return ResponseEntity.status(HttpStatus.OK).body(toResponseDto(updatedBug));
    }

    private BugResponseDto toResponseDto(Bug bug) {
        return new BugResponseDto(
            bug.getId(),
            bug.getTitle(),
            bug.getDescription(),
            bug.getStatus(),
            bug.getSeverity(),
            bug.getCreatedAt(),
            bug.getUser().getId(),
            bug.getUser().getName(),
            bug.getUser().getEmail()
        );
    }
}
