package com.womentech.server.controller;

import com.womentech.server.domain.CompletionStatus;
import com.womentech.server.domain.DailyTask;
import com.womentech.server.domain.Task;
import com.womentech.server.domain.dto.response.DailyTaskResponse;
import com.womentech.server.exception.ErrorResponse;
import com.womentech.server.service.DailyTaskService;
import com.womentech.server.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    @ApiResponse(responseCode = "200", description = "daily 실천 사항 목록",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = DailyTaskResponse.class))))
    public List<DailyTaskResponse> getDailyTasks(@PathVariable Long goal_id, @Parameter LocalDate date) {
        List<DailyTask> dailyTasks = dailyTaskService.findDailyTasksByDate(goal_id, date);
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

    @PatchMapping("/{daily_task_id}/complete")
    @Operation(summary = "daily 실천 사항 완료", description = "daily 실천 사항을 완료합니다.")
    @ApiResponse(responseCode = "200", description = "daily 실천 사항 완료를 성공했습니다.",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class))))
    public ResponseEntity<?> completeDailyTask(@PathVariable Long daily_task_id) {
        dailyTaskService.completeDailyTask(daily_task_id);
        return ResponseEntity.ok()
                .body(new ErrorResponse(HttpStatus.OK.name(), "daily 실천 사항 완료를 성공했습니다."));
    }

    @PatchMapping("/{daily_task_id}/uncomplete")
    @Operation(summary = "daily 실천 사항 완료 취소", description = "daily 실천 사항을 완료 취소합니다.")
    @ApiResponse(responseCode = "200", description = "daily 실천 사항 완료 취소를 성공했습니다.",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class))))
    public ResponseEntity<?> unCompleteDailyTask(@PathVariable Long daily_task_id) {
        dailyTaskService.unCompleteDailyTask(daily_task_id);
        return ResponseEntity.ok()
                .body(new ErrorResponse(HttpStatus.OK.name(), "daily 실천 사항 완료 취소를 성공했습니다."));
    }
}
