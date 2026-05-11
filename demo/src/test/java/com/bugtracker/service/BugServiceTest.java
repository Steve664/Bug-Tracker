package com.bugtracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bugtracker.exception.InvalidOperationException;
import com.bugtracker.model.Bug;
import com.bugtracker.model.Severity;
import com.bugtracker.model.Status;
import com.bugtracker.model.User;
import com.bugtracker.repository.BugRepository;
import com.bugtracker.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BugServiceTest {

    @Mock
    private BugRepository bugRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BugService bugService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("Jane Doe", "jane@example.com");
        ReflectionTestUtils.setField(user, "id", 1L);
    }

    @Test
    void createBug_shouldCreateBugWithOpenStatusAndAssignedUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bugRepository.save(any(Bug.class))).thenAnswer(invocation -> {
            Bug savedBug = invocation.getArgument(0);
            ReflectionTestUtils.setField(savedBug, "id", 100L);
            return savedBug;
        });

        Bug createdBug = bugService.createBug(
            "Login failure",
            "Users cannot log in after submitting valid credentials.",
            Severity.HIGH,
            1L
        );

        assertEquals(100L, createdBug.getId());
        assertEquals("Login failure", createdBug.getTitle());
        assertEquals(Status.OPEN, createdBug.getStatus());
        assertEquals(Severity.HIGH, createdBug.getSeverity());
        assertEquals(user, createdBug.getUser());
        verify(userRepository).findById(1L);
        verify(bugRepository).save(any(Bug.class));
    }

    @Test
    void updateBugStatus_shouldUpdateStatusWhenTransitionIsValid() {
        Bug bug = new Bug(
            "Payment issue",
            "Card payments time out intermittently.",
            Status.OPEN,
            Severity.MEDIUM,
            user
        );
        ReflectionTestUtils.setField(bug, "id", 10L);

        when(bugRepository.findById(10L)).thenReturn(Optional.of(bug));
        when(bugRepository.save(bug)).thenReturn(bug);

        Bug updatedBug = bugService.updateBugStatus(10L, Status.IN_PROGRESS);

        assertEquals(Status.IN_PROGRESS, updatedBug.getStatus());
        verify(bugRepository).findById(10L);
        verify(bugRepository).save(bug);
    }

    @Test
    void updateBugStatus_shouldThrowExceptionWhenTransitionIsInvalid() {
        Bug bug = new Bug(
            "Search bug",
            "Results page shows duplicate records.",
            Status.OPEN,
            Severity.LOW,
            user
        );
        ReflectionTestUtils.setField(bug, "id", 11L);

        when(bugRepository.findById(11L)).thenReturn(Optional.of(bug));

        InvalidOperationException exception = assertThrows(
            InvalidOperationException.class,
            () -> bugService.updateBugStatus(11L, Status.RESOLVED)
        );

        assertEquals(
            "Invalid status transition from OPEN to RESOLVED. Allowed flow: OPEN -> IN_PROGRESS -> RESOLVED -> CLOSED.",
            exception.getMessage()
        );
        verify(bugRepository).findById(11L);
        verify(bugRepository, never()).save(any(Bug.class));
    }
}
