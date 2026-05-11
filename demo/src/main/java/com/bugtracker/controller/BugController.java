package com.bugtracker.controller;

import com.bugtracker.dto.BugCreateRequestDto;
import com.bugtracker.dto.BugResponseDto;
import com.bugtracker.dto.BugUpdateRequestDto;
import com.bugtracker.model.Bug;
import com.bugtracker.service.BugService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Bugs", description = "Operations for creating, viewing, and updating bugs")
public class BugController {

    private final BugService bugService;

    public BugController(BugService bugService) {
        this.bugService = bugService;
    }

    @PostMapping("/create")
    @Operation(summary = "Create a bug", description = "Creates a new bug and assigns it to a user.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Bug created successfully",
            content = @Content(schema = @Schema(implementation = BugResponseDto.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid request payload", content = @Content),
        @ApiResponse(responseCode = "404", description = "Assigned user not found", content = @Content)
    })
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
    @Operation(summary = "List all bugs", description = "Returns all bugs currently stored in the system.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Bugs returned successfully",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BugResponseDto.class)))
        )
    })
    public ResponseEntity<List<BugResponseDto>> getAllBugs() {
        List<BugResponseDto> bugs = bugService.getAllBugs()
            .stream()
            .map(this::toResponseDto)
            .toList();

        return ResponseEntity.ok(bugs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a bug by id", description = "Fetches a single bug using its identifier.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Bug returned successfully",
            content = @Content(schema = @Schema(implementation = BugResponseDto.class))
        ),
        @ApiResponse(responseCode = "404", description = "Bug not found", content = @Content)
    })
    public ResponseEntity<BugResponseDto> getBugById(
        @Parameter(description = "Unique id of the bug", example = "1") @PathVariable Long id
    ) {
        Bug bug = bugService.getBugById(id);
        return ResponseEntity.ok(toResponseDto(bug));
    }

    @PutMapping("/{id}/status")
    @Operation(
        summary = "Update bug status",
        description = "Updates the bug status following the allowed workflow: OPEN -> IN_PROGRESS -> RESOLVED -> CLOSED."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Bug status updated successfully",
            content = @Content(schema = @Schema(implementation = BugResponseDto.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid status transition or request payload", content = @Content),
        @ApiResponse(responseCode = "404", description = "Bug not found", content = @Content)
    })
    public ResponseEntity<BugResponseDto> updateBugStatus(
        @Parameter(description = "Unique id of the bug", example = "1") @PathVariable Long id,
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
