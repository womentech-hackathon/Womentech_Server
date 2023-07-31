package com.womentech.server.service;

import com.womentech.server.domain.CompletionStatus;
import com.womentech.server.domain.DailyTask;
import com.womentech.server.domain.Goal;
import com.womentech.server.domain.Task;
import com.womentech.server.domain.dto.request.TaskRequest;
import com.womentech.server.repository.DailyTaskRepository;
import com.womentech.server.repository.GoalRepository;
import com.womentech.server.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.womentech.server.domain.CompletionStatus.COMPLETE;
import static com.womentech.server.domain.CompletionStatus.PROGRESS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {
    private final GoalRepository goalRepository;
    private final TaskRepository taskRepository;
    private final DailyTaskRepository dailyTaskRepository;

    public List<Task> findTasks(Long goalId) {
        return taskRepository.findByGoalId(goalId);
    }

    public Task findTask(Long taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    public int countTasks(Long goalId, CompletionStatus status) {
        return taskRepository.countByGoalIdAndStatus(goalId, status);
    }

    @Transactional
    public void addTask(Long goalId, TaskRequest dto) {
        Optional<Goal> goal = goalRepository.findById(goalId);
        goal.ifPresent(g -> {
            Task task = Task.builder()
                    .goal(goal.orElse(null))
                    .name(dto.getName())
                    .days(dto.getDays())
                    .startDate(LocalDate.now())
                    .status(PROGRESS)
                    .build();
            taskRepository.save(task);

            DailyTask dailyTask = DailyTask.builder()
                    .goal(goal.orElse(null))
                    .taskId(task.getId())
                    .date(LocalDate.now())
                    .status(PROGRESS)
                    .build();
            dailyTaskRepository.save(dailyTask);
        });
    }

    @Transactional
    public void updateTask(Long taskId, TaskRequest taskRequest) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (taskRequest.getName() != null) {
            task.setName(taskRequest.getName());
        }
        if (taskRequest.getDays() != null) {
            task.setDays(taskRequest.getDays());
        }
    }

    @Transactional
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
        dailyTaskRepository.deleteByTaskIdAndDate(taskId, LocalDate.now());
    }

    @Transactional
    public void setTaskStatus(Long task_id, CompletionStatus status) {
        Task task = taskRepository.findById(task_id).orElse(null);
        if (status == COMPLETE) {
            task.complete();
        } else {
            task.unComplete();
        }
    }
}
