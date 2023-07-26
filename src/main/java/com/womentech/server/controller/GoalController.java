package com.womentech.server.controller;

import com.womentech.server.domain.dto.request.GoalAddRequest;
import com.womentech.server.domain.dto.request.GoalUpdateRequest;
import com.womentech.server.domain.dto.request.TaskRequest;
import com.womentech.server.domain.dto.response.GoalCompletedResponse;
import com.womentech.server.domain.dto.response.GoalProgressResponse;
import com.womentech.server.exception.ErrorCode;
import com.womentech.server.exception.ErrorResponse;
import com.womentech.server.service.GoalService;
import com.womentech.server.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "goal", description = "목표 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/goals")
public class GoalController {
    private final GoalService goalService;
    private final TaskService taskService;

    @GetMapping("/progress")
    @Operation(summary = "목표 조회(달성 중)", description = "달성 중인 목표를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "OK", description = "달성 중인 목표 조회를 성공했습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GoalProgressResponse.class))),
            @ApiResponse(responseCode = "GOAL_NOT_FOUND", description = "달성 중인 목표가 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public GoalProgressResponse getProgressGoal(Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        return goalService.getProgressGoal(userId);
    }

    @GetMapping("/completed")
    @Operation(summary = "목표 조회(달성 완료)", description = "달성 완료한 목표를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "OK", description = "달성 완료한 목표 조회를 성공했습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GoalCompletedResponse.class))),
            @ApiResponse(responseCode = "GOAL_NOT_FOUND", description = "달성 완료한 목표가 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<GoalCompletedResponse> getCompletedGoal(Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        return goalService.getCompletedGoals(userId);
    }

    @PostMapping()
    @Operation(summary = "목표 추가", description = "목표를 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "OK", description = "목표 추가를 성공했습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "BAD_REQUEST", description = "이름을 입력해주세요.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> addGoal(@RequestBody GoalAddRequest dto, Authentication authentication) {
        if (dto.getName() == null) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(ErrorCode.BAD_REQUEST.name(), "이름을 입력해주세요."));
        }
        Long userId = Long.valueOf(authentication.getName());
        Long goal_id = goalService.addGoal(userId, dto);
        for (TaskRequest task : dto.getTasks()) {
            taskService.addTask(goal_id, new TaskRequest(task.getName(), task.getDays()));
        }
        return ResponseEntity.ok()
                .body(new ErrorResponse(HttpStatus.OK.name(), "목표 추가를 성공했습니다."));
    }

    @PatchMapping("/{goal_id}")
    @Operation(summary = "목표 수정", description = "목표를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "OK", description = "목표 수정을 성공했습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "BAD_REQUEST", description = "이름을 입력해주세요.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> updateGoal(@PathVariable Long goal_id, @RequestBody GoalUpdateRequest dto) {
        if (dto.getName() == null) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(ErrorCode.BAD_REQUEST.name(), "이름을 입력해주세요."));
        }
        goalService.updateGoal(goal_id, dto);
        return ResponseEntity.ok()
                .body(new ErrorResponse(HttpStatus.OK.name(), "목표 수정을 성공했습니다."));
    }

    @DeleteMapping("/{goal_id}")
    @Operation(summary = "목표 삭제", description = "목표를 삭제합니다.")
    @ApiResponse(responseCode = "OK", description = "목표 삭제를 성공했습니다.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    public ResponseEntity<?> deleteGoal(@PathVariable Long goal_id) {
        goalService.deleteGoal(goal_id);
        return ResponseEntity.ok()
                .body(new ErrorResponse(HttpStatus.OK.name(), "목표 삭제를 성공했습니다."));
    }
}
