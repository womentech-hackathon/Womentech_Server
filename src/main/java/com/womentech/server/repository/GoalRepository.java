package com.womentech.server.repository;

import com.womentech.server.domain.CompletionStatus;
import com.womentech.server.domain.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByUserIdAndStatus(Long userId, CompletionStatus status);
}
