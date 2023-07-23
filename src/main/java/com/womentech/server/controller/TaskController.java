package com.womentech.server.controller;

import com.womentech.server.domain.dto.TaskRequest;
import com.womentech.server.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "task", description = "실천 사항 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/goals/{goal_id}/tasks")
public class TaskController {
    private final TaskService taskService;

    @PostMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "실천 사항 추가", description = "목표의 실천 사항을 추가합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 추가합니다.")
    public void addTask(@PathVariable Long goal_id, @RequestBody TaskRequest dto) {
        taskService.addTask(goal_id, dto);
    }

    @PatchMapping("/{task_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "실천 사항 수정", description = "목표의 실천 사항을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 수정합니다.")
    public void updateTask(@PathVariable Long task_id, @RequestBody TaskRequest dto) {
        taskService.updateTask(task_id, dto);
    }

    @DeleteMapping("/{task_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "실천 사항 삭제", description = "목표의 실천 사항을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 삭제합니다.")
    public void deleteTask(@PathVariable Long task_id) {
        taskService.deleteTask(task_id);
    }

    @PatchMapping("/{task_id}/complete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "실천 사항 완료", description = "목표의 실천 사항을 완료합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 완료합니다.")
    public void completeTask(@PathVariable Long task_id) {
        taskService.completeTask(task_id);
    }

    @PatchMapping("/{task_id}/uncomplete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "실천 사항 완료 취소", description = "목표의 실천 사항을 완료 취소합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 완료 취소합니다.")
    public void unCompleteTask(@PathVariable Long task_id) {
        taskService.unCompleteTask(task_id);
    }
}
