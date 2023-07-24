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
}
