package com.bugtracker.service;

import com.bugtracker.exception.InvalidOperationException;
import com.bugtracker.exception.ResourceNotFoundException;
import com.bugtracker.model.Bug;
import com.bugtracker.model.Severity;
import com.bugtracker.model.Status;
import com.bugtracker.model.User;
import com.bugtracker.repository.BugRepository;
import com.bugtracker.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BugService {

    private final BugRepository bugRepository;
    private final UserRepository userRepository;

    public BugService(BugRepository bugRepository, UserRepository userRepository) {
        this.bugRepository = bugRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Bug createBug(String title, String description, Severity severity, Long userId) {
        User assignedUser = findUserById(userId);
        Bug bug = new Bug(title, description, Status.OPEN, severity, assignedUser);
        return bugRepository.save(bug);
    }

    @Transactional
    public Bug updateBugStatus(Long bugId, Status newStatus) {
        Bug bug = getBugById(bugId);
        validateStatusTransition(bug.getStatus(), newStatus);
        bug.setStatus(newStatus);
        return bugRepository.save(bug);
    }

    @Transactional
    public Bug assignBugToUser(Long bugId, Long userId) {
        Bug bug = getBugById(bugId);
        User assignedUser = findUserById(userId);
        bug.setUser(assignedUser);
        return bugRepository.save(bug);
    }

    public List<Bug> getAllBugs() {
        return bugRepository.findAll();
    }

    public Bug getBugById(Long bugId) {
        return bugRepository.findById(bugId)
            .orElseThrow(() -> new ResourceNotFoundException("Bug not found with id: " + bugId));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    private void validateStatusTransition(Status currentStatus, Status newStatus) {
        if (currentStatus == newStatus) {
            throw new InvalidOperationException(
                "Bug is already in status " + newStatus + "."
            );
        }

        boolean isValidTransition =
            (currentStatus == Status.OPEN && newStatus == Status.IN_PROGRESS)
                || (currentStatus == Status.IN_PROGRESS && newStatus == Status.RESOLVED)
                || (currentStatus == Status.RESOLVED && newStatus == Status.CLOSED);

        if (!isValidTransition) {
            throw new InvalidOperationException(
                "Invalid status transition from " + currentStatus + " to " + newStatus
                    + ". Allowed flow: OPEN -> IN_PROGRESS -> RESOLVED -> CLOSED."
            );
        }
    }
}
