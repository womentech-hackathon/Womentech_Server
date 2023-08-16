package com.womentech.server.repository;

import com.womentech.server.domain.DailyTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyTaskRepository extends JpaRepository<DailyTask, Long> {
    // 오늘 날짜의 task_id에 해당하는 데이터를 삭제하는 메서드
    void deleteByTaskIdAndDate(Long taskId, LocalDate date);

    List<DailyTask> findByGoalIdAndDate(Long goalId, LocalDate date);

    int countByGoalIdAndDate(Long goalId, LocalDate date);
}
