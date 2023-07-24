package com.womentech.server.controller;

import com.womentech.server.domain.CompletionStatus;
import com.womentech.server.domain.DailyTask;
import com.womentech.server.domain.Task;
import com.womentech.server.domain.dto.response.DailyTaskResponse;
import com.womentech.server.service.DailyTaskService;
import com.womentech.server.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "daily_task", description = "daily 실천 사항 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/goals/{goal_id}/daily-tasks")
public class DailyTaskController {
    private final DailyTaskService dailyTaskService;
    private final TaskService taskService;

    @GetMapping()
    @Operation(summary = "daily 실천 사항 조회", description = "daily 실천 사항을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 조회합니다.")
    public List<DailyTaskResponse> getDailyTasks(@PathVariable Long goal_id) {
        List<DailyTask> dailyTasks = dailyTaskService.findDailyTasks(goal_id);
        List<DailyTaskResponse> dto = dailyTasks.stream()
                .map(dailyTask -> {
                    Task task = taskService.findTask(dailyTask.getTaskId());
                    return new DailyTaskResponse(
                            dailyTask.getId(),
                            task.getName(),
                            task.getDays(),
                            task.getStatus() == CompletionStatus.COMPLETE ? task.getStatus() : dailyTask.getStatus());
                })
                .collect(Collectors.toList());
        return dto;
    }
}
