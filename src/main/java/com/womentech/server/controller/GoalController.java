package com.womentech.server.controller;

import com.womentech.server.domain.dto.request.GoalAddRequest;
import com.womentech.server.domain.dto.request.GoalUpdateRequest;
import com.womentech.server.domain.dto.request.TaskRequest;
import com.womentech.server.exception.Code;
import com.womentech.server.exception.GeneralException;
import com.womentech.server.exception.dto.DataResponse;
import com.womentech.server.service.GoalService;
import com.womentech.server.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "goal", description = "목표 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/goals")
public class GoalController {
    private final GoalService goalService;
    private final TaskService taskService;

    @GetMapping("/progress")
    @Operation(summary = "목표 조회 (달성 중)", description = "달성 중인 목표를 조회합니다.")
    public DataResponse<Object> getProgressGoal(Authentication authentication) {
        String username = authentication.getName();

        return DataResponse.of(goalService.getProgressGoal(username));
    }

    @GetMapping("/completed")
    @Operation(summary = "목표 조회 (달성 완료)", description = "달성 완료한 목표를 조회합니다.")
    public DataResponse<Object> getCompletedGoal(Authentication authentication) {
        String username = authentication.getName();

        return DataResponse.of(goalService.getCompletedGoals(username));
    }

    @PostMapping()
    @Operation(summary = "목표 추가", description = "목표를 추가합니다.")
    public DataResponse<Object> addGoal(@RequestBody GoalAddRequest goalAddRequest, Authentication authentication) {
        if (goalAddRequest.getName() == null) {
            throw new GeneralException(Code.BAD_REQUEST, "Goal name is empty");
        }
        String username = authentication.getName();
        Long goalId = goalService.addGoal(username, goalAddRequest);
        for (TaskRequest task : goalAddRequest.getTasks()) {
            taskService.addTask(goalId, new TaskRequest(task.getName(), task.getDays()));
        }

        return DataResponse.empty();
    }

    @PatchMapping("/{goal_id}/edit")
    @Operation(summary = "목표 수정", description = "목표를 수정합니다.")
    public DataResponse<Object> updateGoal(@PathVariable("goal_id") Long goalId, @RequestBody GoalUpdateRequest goalUpdateRequest) {
        if (goalUpdateRequest.getName() == null) {
            throw new GeneralException(Code.BAD_REQUEST, "Goal name is empty");
        }
        goalService.updateGoal(goalId, goalUpdateRequest);

        return DataResponse.empty();
    }

    @DeleteMapping("/{goal_id}")
    @Operation(summary = "목표 삭제", description = "목표를 삭제합니다.")
    public DataResponse<Object> deleteGoal(@PathVariable("goal_id") Long goalId) {
        goalService.deleteGoal(goalId);

        return DataResponse.empty();
    }

    @PatchMapping("/{goal_id}")
    @Operation(summary = "목표 완료", description = "목표를 완료합니다.")
    public DataResponse<Object> setGoalStatus(@PathVariable("goal_id") Long goalId) {
        goalService.setGoalStatus(goalId);

        return DataResponse.empty();
    }
}
