package com.womentech.server.service;

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
import java.util.Optional;

import static com.womentech.server.domain.CompletionStatus.PROGRESS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {
    private final GoalRepository goalRepository;
    private final TaskRepository taskRepository;
    private final DailyTaskRepository dailyTaskRepository;

    public Task findTask(Long task_id) {
        return taskRepository.findById(task_id).orElse(null);
    }

    @Transactional
    public void addTask(Long goal_id, TaskRequest dto) {
        Optional<Goal> goal = goalRepository.findById(goal_id);
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
    public void updateTask(Long task_id, TaskRequest dto) {
        Task task = taskRepository.findById(task_id).orElse(null);
        if (dto.getName() != null) {
            task.setName(dto.getName());
        }
        if (dto.getDays() != null) {
            task.setDays(dto.getDays());
        }
        taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Long task_id) {
        taskRepository.deleteById(task_id);
        dailyTaskRepository.deleteByTaskIdAndDate(task_id, LocalDate.now());
    }

    @Transactional
    public void completeTask(Long task_id) {
        Task task = taskRepository.findById(task_id).orElse(null);
        task.complete();
        taskRepository.save(task);
    }

    @Transactional
    public void unCompleteTask(Long task_id) {
        Task task = taskRepository.findById(task_id).orElse(null);
        task.unComplete();
        taskRepository.save(task);
    }
}
