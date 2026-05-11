package com.bugtracker.repository;

import com.bugtracker.model.Bug;
import com.bugtracker.model.Severity;
import com.bugtracker.model.Status;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BugRepository extends JpaRepository<Bug, Long> {

    List<Bug> findByStatus(Status status);

    List<Bug> findBySeverity(Severity severity);
}
