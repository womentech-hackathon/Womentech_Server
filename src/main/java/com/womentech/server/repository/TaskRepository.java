package com.womentech.server.repository;

import com.womentech.server.domain.CompletionStatus;
import com.womentech.server.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(CompletionStatus status);
}
