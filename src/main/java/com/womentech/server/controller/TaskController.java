package com.womentech.server.controller;

import com.womentech.server.domain.CompletionStatus;
import com.womentech.server.domain.Task;
import com.womentech.server.domain.dto.request.TaskRequest;
import com.womentech.server.domain.dto.response.TaskResponse;
import com.womentech.server.exception.Code;
import com.womentech.server.exception.GeneralException;
import com.womentech.server.exception.dto.DataResponse;
import com.womentech.server.exception.dto.Response;
import com.womentech.server.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "task", description = "실천 사항 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/goals/{goal_id}/tasks")
public class TaskController {
    private final TaskService taskService;

    @GetMapping()
    @Operation(summary = "실천 사항 조회", description = "실천 사항을 조회합니다.")
    public DataResponse<Object> getTasks(@PathVariable("goal_id") Long goalId) {
        List<Task> tasks = taskService.findTasks(goalId);
        List<TaskResponse> taskResponses = tasks.stream()
                .map(task -> new TaskResponse(
                        task.getName(),
                        task.getStartDate(),
                        task.getDays()))
                .collect(Collectors.toList());

        return DataResponse.of(taskResponses);
    }

    @GetMapping("/count")
    @Operation(summary = "실천 사항 개수 조회", description = "실천 사항 개수를 조회합니다.")
    public DataResponse<Object> countTasks(@PathVariable("goal_id") Long goalId, @Parameter CompletionStatus status) {
        return DataResponse.of(taskService.countTasks(goalId, status));
    }

    @PostMapping()
    @Operation(summary = "실천 사항 추가", description = "실천 사항을 추가합니다.")
    public DataResponse<Object> addTask(@PathVariable("goal_id") Long goalId, @RequestBody List<TaskRequest> taskRequests) {
        // Task에 이름이 있는지 확인
        for (TaskRequest taskRequest : taskRequests) {
            if (taskRequest.getName() == null) {
                throw new GeneralException(Code.BAD_REQUEST, "Task name is empty");
            }
        }

        for (TaskRequest taskRequest : taskRequests) {
            taskService.addTask(goalId, taskRequest);
        }

        return DataResponse.empty();
    }

    @PatchMapping("/{task_id}/edit")
    @Operation(summary = "실천 사항 수정", description = "실천 사항을 수정합니다.")
    public DataResponse<Object> updateTask(@PathVariable("task_id") Long taskId, @RequestBody TaskRequest taskRequest) {
        if (taskRequest.getName() == null) {
            throw new GeneralException(Code.BAD_REQUEST, "Task name is empty");
        }
        taskService.updateTask(taskId, taskRequest);

        return DataResponse.empty();
    }

    @DeleteMapping("/{task_id}")
    @Operation(summary = "실천 사항 삭제", description = "실천 사항을 삭제합니다.")
    @ApiResponse(responseCode = "OK", description = "실천 사항 삭제를 성공했습니다.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Response.class)))
    public DataResponse<Object> deleteTask(@PathVariable("task_id") Long taskId) {
        taskService.deleteTask(taskId);

        return DataResponse.empty();
    }

    @PatchMapping("/{task_id}")
    @Operation(summary = "실천 사항 완료/완료 취소", description = "목표의 실천 사항을 완료/완료 취소합니다.")
    public DataResponse<Object> setTaskStatus(@PathVariable("task_id") Long taskId, @Parameter CompletionStatus status) {
        taskService.setTaskStatus(taskId, status);

        return DataResponse.empty();
    }
}
