package com.womentech.server.service;

import com.womentech.server.domain.Goal;
import com.womentech.server.domain.Task;
import com.womentech.server.domain.User;
import com.womentech.server.domain.dto.request.GoalAddRequest;
import com.womentech.server.domain.dto.request.GoalUpdateRequest;
import com.womentech.server.domain.dto.response.GoalCompletedResponse;
import com.womentech.server.domain.dto.response.GoalProgressResponse;
import com.womentech.server.domain.dto.response.TaskCompletedResponse;
import com.womentech.server.domain.dto.response.TaskProgressResponse;
import com.womentech.server.exception.GeneralException;
import com.womentech.server.exception.Code;
import com.womentech.server.repository.GoalRepository;
import com.womentech.server.repository.TaskRepository;
import com.womentech.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.womentech.server.domain.CompletionStatus.COMPLETE;
import static com.womentech.server.domain.CompletionStatus.PROGRESS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoalService {
    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final TaskRepository taskRepository;

    public GoalProgressResponse getProgressGoal(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        List<Goal> goals = goalRepository.findByUserIdAndStatus(user.getId(), PROGRESS);
        if (goals.isEmpty()) {
            throw new GeneralException(Code.NOT_FOUND, "달성 중인 목표가 없습니다.");
        }
        Goal goal = goals.get(0);
        List<Task> tasks = taskRepository.findByGoalId(goal.getId());
        List<TaskProgressResponse> dto = tasks.stream()
                .map(task -> new TaskProgressResponse(
                        task.getId(),
                        task.getName(),
                        task.getDays(),
                        task.getStatus()))
                .collect(Collectors.toList());
        return new GoalProgressResponse(
                goal.getId(),
                goal.getName(),
                goal.getStartDate(),
                dto);
    }

    public List<GoalCompletedResponse> getCompletedGoals(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        List<GoalCompletedResponse> result = new ArrayList<>();
        List<Goal> goals = goalRepository.findByUserIdAndStatus(user.getId(), COMPLETE);
        if (goals.isEmpty()) {
            throw new GeneralException(Code.NOT_FOUND, "달성 완료한 목표가 없습니다.");
        }
        for (Goal goal : goals) {
            List<Task> tasks = taskRepository.findByGoalId(goal.getId());
            List<TaskCompletedResponse> dto = tasks.stream()
                    .map(task -> new TaskCompletedResponse(
                            task.getId(),
                            task.getName(),
                            task.getDays(),
                            task.getStartDate(),
                            task.getEndDate()))
                    .collect(Collectors.toList());
            result.add(new GoalCompletedResponse(
                    goal.getId(),
                    goal.getName(),
                    goal.getStartDate(),
                    goal.getEndDate(),
                    dto));
        }
        return result;
    }

    @Transactional
    public Long addGoal(String username, GoalAddRequest dto) {
        User user = userRepository.findByUsername(username).orElse(null);
        Goal goal = Goal.builder()
                .user(user)
                .name(dto.getName())
                .startDate(LocalDate.now())
                .status(PROGRESS)
                .build();
        goalRepository.save(goal);
        return goal.getId();
    }

    @Transactional
    public void updateGoal(Long goalId, GoalUpdateRequest dto) {
        Goal goal = goalRepository.findById(goalId).orElse(null);
        goal.setName(dto.getName());
        goalRepository.save(goal);
    }

    @Transactional
    public void deleteGoal(Long goalId) {
        goalRepository.deleteById(goalId);
    }

    @Transactional
    public void setGoalStatus(Long goalId) {
        Goal goal = goalRepository.findById(goalId).orElse(null);
        goal.complete();

        List<Task> tasks = taskRepository.findByGoalId(goalId);
        for (Task task : tasks) {
            task.complete();
        }
    }
}
