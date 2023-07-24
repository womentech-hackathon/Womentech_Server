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

    @Transactional
    public void completeDailyTask(Long daily_task_id) {
        DailyTask dailyTask = dailyTaskRepository.findById(daily_task_id).orElse(null);
        dailyTask.complete();
        dailyTaskRepository.save(dailyTask);
    }

    @Transactional
    public void unCompleteDailyTask(Long daily_task_id) {
        DailyTask dailyTask = dailyTaskRepository.findById(daily_task_id).orElse(null);
        dailyTask.unComplete();
        dailyTaskRepository.save(dailyTask);
    }
}
