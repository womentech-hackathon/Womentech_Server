package com.womentech.server.service;

import com.womentech.server.domain.DailyTask;
import com.womentech.server.repository.DailyTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DailyTaskService {
    private final DailyTaskRepository dailyTaskRepository;

    public List<DailyTask> findDailyTasks(Long goal_id) {
        return dailyTaskRepository.findByGoalId(goal_id);
    }
}
