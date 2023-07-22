package com.womentech.server.controller;

import com.womentech.server.domain.dto.UserJoinRequest;
import com.womentech.server.domain.dto.UserLoginRequest;
import com.womentech.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "회원가입합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 회원가입합니다.",
            content = @Content(mediaType = "text/plain",
                    schema = @Schema(implementation = String.class)))
    public ResponseEntity<String> join(@RequestBody UserJoinRequest dto) {
        userService.join(dto.getIdentifier(), dto.getName(), dto.getPassword());
        return ResponseEntity.ok().body("회원가입을 성공했습니다.");
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 로그인합니다.",
            content = @Content(mediaType = "text/plain",
                    schema = @Schema(implementation = String.class)))
    public ResponseEntity<String> login(@RequestBody UserLoginRequest dto) {
        String token = userService.login(dto.getIdentifier(), dto.getPassword());
        return ResponseEntity.ok().body(token);
    }
}
