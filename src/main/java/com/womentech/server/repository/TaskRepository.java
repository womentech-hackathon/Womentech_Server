package com.womentech.server.repository;

import com.womentech.server.domain.CompletionStatus;
import com.womentech.server.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // 상태 정보를 기준으로 조회하는 메서드
    List<Task> findByStatus(CompletionStatus status);

    // goal_id에 해당하는 task들을 조회하는 메서드
    List<Task> findByGoalId(Long goalId);

    // task의 상태에 따른 개수를 조회하는 메서드
    int countByGoalIdAndStatus(Long goalId, CompletionStatus status);
}
