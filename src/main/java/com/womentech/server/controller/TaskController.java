package com.womentech.server.controller;

import com.womentech.server.domain.dto.request.TaskRequest;
import com.womentech.server.exception.ErrorCode;
import com.womentech.server.exception.ErrorResponse;
import com.womentech.server.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "task", description = "실천 사항 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/goals/{goal_id}/tasks")
public class TaskController {
    private final TaskService taskService;

    @PostMapping()
    @Operation(summary = "실천 사항 추가", description = "목표의 실천 사항을 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "OK", description = "실천 사항 추가를 성공했습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "BAD_REQUEST", description = "이름을 입력해주세요.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> addTask(@PathVariable Long goal_id, @RequestBody TaskRequest dto) {
        if (dto.getName() == null) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(ErrorCode.BAD_REQUEST.name(), "이름을 입력해주세요."));
        }
        taskService.addTask(goal_id, dto);
        return ResponseEntity.ok()
                .body(new ErrorResponse(HttpStatus.OK.name(), "실천 사항 추가를 성공했습니다."));
    }

    @PatchMapping("/{task_id}")
    @Operation(summary = "실천 사항 수정", description = "목표의 실천 사항을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "OK", description = "실천 사항 수정을 성공했습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "BAD_REQUEST", description = "이름을 입력해주세요.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> updateTask(@PathVariable Long task_id, @RequestBody TaskRequest dto) {
        if (dto.getName() == null) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(ErrorCode.BAD_REQUEST.name(), "이름을 입력해주세요."));
        }
        taskService.updateTask(task_id, dto);
        return ResponseEntity.ok()
                .body(new ErrorResponse(HttpStatus.OK.name(), "실천 사항 수정을 성공했습니다."));
    }

    @DeleteMapping("/{task_id}")
    @Operation(summary = "실천 사항 삭제", description = "목표의 실천 사항을 삭제합니다.")
    @ApiResponse(responseCode = "OK", description = "실천 사항 삭제를 성공했습니다.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<?> deleteTask(@PathVariable Long task_id) {
        taskService.deleteTask(task_id);
        return ResponseEntity.ok()
                .body(new ErrorResponse(HttpStatus.OK.name(), "실천 사항 삭제를 성공했습니다."));
    }

    @PatchMapping("/{task_id}/complete")
    @Operation(summary = "실천 사항 완료", description = "목표의 실천 사항을 완료합니다.")
    @ApiResponse(responseCode = "OK", description = "실천 사항 완료를 성공했습니다.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<?> completeTask(@PathVariable Long task_id) {
        taskService.completeTask(task_id);
        return ResponseEntity.ok()
                .body(new ErrorResponse(HttpStatus.OK.name(), "실천 사항 완료를 성공했습니다."));
    }

    @PatchMapping("/{task_id}/uncomplete")
    @Operation(summary = "실천 사항 완료 취소", description = "목표의 실천 사항을 완료 취소합니다.")
    @ApiResponse(responseCode = "OK", description = "실천 사항 완료 취소를 성공했습니다.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<?> unCompleteTask(@PathVariable Long task_id) {
        taskService.unCompleteTask(task_id);
        return ResponseEntity.ok()
                .body(new ErrorResponse(HttpStatus.OK.name(), "실천 사항 완료 취소를 성공했습니다."));
    }
}
