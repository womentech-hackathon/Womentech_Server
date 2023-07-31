package com.womentech.server.service;

import com.womentech.server.domain.CompletionStatus;
import com.womentech.server.domain.DailyTask;
import com.womentech.server.repository.DailyTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.womentech.server.domain.CompletionStatus.COMPLETE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DailyTaskService {
    private final DailyTaskRepository dailyTaskRepository;

    public List<DailyTask> findDailyTasks(Long goalId, LocalDate date) {
        return dailyTaskRepository.findByGoalIdAndDate(goalId, date);
    }

    @Transactional
    public void setDailyTaskStatus(Long dailyTaskId, CompletionStatus status) {
        DailyTask dailyTask = dailyTaskRepository.findById(dailyTaskId).orElse(null);
        if (status == COMPLETE) {
            dailyTask.complete();
        } else {
            dailyTask.unComplete();
        }
    }
}
