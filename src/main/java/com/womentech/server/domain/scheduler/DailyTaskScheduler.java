package com.womentech.server.domain.scheduler;

import com.womentech.server.domain.CompletionStatus;
import com.womentech.server.domain.DailyTask;
import com.womentech.server.domain.Task;
import com.womentech.server.repository.DailyTaskRepository;
import com.womentech.server.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DailyTaskScheduler {
    private final TaskRepository taskRepository;
    private final DailyTaskRepository dailyTaskRepository;

    @Autowired
    public DailyTaskScheduler(TaskRepository taskRepository, DailyTaskRepository dailyTaskRepository) {
        this.taskRepository = taskRepository;
        this.dailyTaskRepository = dailyTaskRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정(00:00)에 실행
    public void addInProgressTasksToDailyTask() {
        // task 테이블에서 status가 PROGRESS인 데이터 조회
        List<Task> inProgressTasks = taskRepository.findByStatus(CompletionStatus.PROGRESS);

        // 조회된 데이터를 daily_task 테이블에 추가
        for (Task task : inProgressTasks) {
            DailyTask dailyTask = new DailyTask();
            dailyTask.setDate(LocalDate.now());
            dailyTask.setTask(task);
            dailyTaskRepository.save(dailyTask);
        }

        System.out.println(inProgressTasks.size() + " tasks added to daily_task.");
    }
}
