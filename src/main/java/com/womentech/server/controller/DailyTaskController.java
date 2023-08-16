package com.womentech.server.controller;

import com.womentech.server.domain.CompletionStatus;
import com.womentech.server.domain.DailyTask;
import com.womentech.server.domain.Task;
import com.womentech.server.domain.dto.response.CountResponse;
import com.womentech.server.domain.dto.response.DailyTaskResponse;
import com.womentech.server.exception.dto.DataResponse;
import com.womentech.server.service.DailyTaskService;
import com.womentech.server.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "daily_task", description = "데일리 실천 사항 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/goals/{goal_id}/daily-tasks")
public class DailyTaskController {
    private final DailyTaskService dailyTaskService;
    private final TaskService taskService;

    @GetMapping()
    @Operation(summary = "daily 실천 사항 조회", description = "daily 실천 사항을 조회합니다.")
    public DataResponse<Object> getDailyTasks(@PathVariable("goal_id") Long goalId, @Parameter LocalDate date) {
        List<DailyTask> dailyTasks = dailyTaskService.findDailyTasks(goalId, date);
        List<DailyTaskResponse> dailyTaskResponses = dailyTasks.stream()
                .map(dailyTask -> {
                    Task task = taskService.findTask(dailyTask.getTaskId());
                    return new DailyTaskResponse(
                            dailyTask.getId(),
                            task.getName(),
                            task.getStartDate(),
                            task.getDays(),
                            task.getStatus() == CompletionStatus.COMPLETE ? task.getStatus() : dailyTask.getStatus());
                })
                .collect(Collectors.toList());

        return DataResponse.of(dailyTaskResponses);
    }

    @GetMapping("/count")
    @Operation(summary = "daily 실천 사항 개수 조회", description = "daily 실천 사항 개수를 조회합니다.")
    public DataResponse<Object> countDailyTasks(@PathVariable("goal_id") Long goalId) {
        return DataResponse.of(new CountResponse(dailyTaskService.countDailyTasks(goalId)));
    }

    @PatchMapping("/{daily_task_id}")
    @Operation(summary = "daily 실천 사항 완료/완료 취소", description = "daily 실천 사항을 완료/완료 취소합니다.")
    public DataResponse<Object> setDailyTaskStatus(@PathVariable("daily_task_id") Long dailyTaskId, @Parameter CompletionStatus status) {
        dailyTaskService.setDailyTaskStatus(dailyTaskId, status);

        return DataResponse.empty();
    }
}
