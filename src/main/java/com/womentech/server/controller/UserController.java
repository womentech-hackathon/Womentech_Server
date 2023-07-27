package com.womentech.server.controller;

import com.womentech.server.domain.dto.request.UserJoinRequest;
import com.womentech.server.domain.dto.request.UserLoginRequest;
import com.womentech.server.exception.ErrorResponse;
import com.womentech.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "user", description = "사용자 API")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "회원가입합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "OK", description = "회원가입을 성공했습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "IDENTIFIER_DUPLICATED", description = "이미 존재하는 아이디입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> join(@RequestBody UserJoinRequest dto) {
        userService.join(dto.getIdentifier(), dto.getName(), dto.getPassword());
        return ResponseEntity.ok()
                .body(new ErrorResponse(HttpStatus.OK.name(), "회원가입을 성공했습니다."));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "OK", description = "token 값",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "IDENTIFIER_NOT_FOUND", description = "존재하지 않는 아이디입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "INVALID_PASSWORD", description = "패스워드를 잘못 입력했습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<?> login(@RequestBody UserLoginRequest dto) {
        String token = userService.login(dto.getIdentifier(), dto.getPassword());
        return ResponseEntity.ok()
                .body(new ErrorResponse(HttpStatus.OK.name(), token));
    }

    @DeleteMapping("/users")
    public ResponseEntity<?> deleteUser(Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        userService.deleteUserById(userId);
        return ResponseEntity.ok()
                .body(new ErrorResponse(HttpStatus.OK.name(), "회원탈퇴를 성공했습니다."));
    }
}
