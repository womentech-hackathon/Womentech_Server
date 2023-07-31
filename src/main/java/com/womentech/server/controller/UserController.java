package com.womentech.server.controller;

import com.womentech.server.domain.dto.request.UserJoinRequest;
import com.womentech.server.domain.dto.request.UserLoginRequest;
import com.womentech.server.domain.dto.request.UserNameRequest;
import com.womentech.server.domain.dto.request.UserPasswordRequest;
import com.womentech.server.exception.dto.DataResponse;
import com.womentech.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "user", description = "사용자 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "회원가입합니다.")
    public DataResponse<Object> join(@RequestBody UserJoinRequest userJoinRequest) {
        userService.join(userJoinRequest.getUsername(), userJoinRequest.getName(), userJoinRequest.getPassword());

        return DataResponse.empty();
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인합니다.")
    public DataResponse<Object> login(@RequestBody UserLoginRequest userLoginRequest) {
        return DataResponse.of(userService.login(userLoginRequest.getUsername(), userLoginRequest.getPassword()));
    }

    @PostMapping("/logout")
    public DataResponse<Object> logout() {
        // Todo: 로그아웃 구현

        return DataResponse.empty();
    }

    @DeleteMapping("/withdrawal")
    public DataResponse<Object> deleteUser(Authentication authentication) {
        String username = authentication.getName();
        userService.deleteUser(username);

        return DataResponse.empty();
    }

    @PatchMapping("/name/edit")
    public DataResponse<Object> updateName(@RequestBody UserNameRequest userNameRequest, Authentication authentication) {
        String username = authentication.getName();
        userService.editName(username, userNameRequest.getName());

        return DataResponse.empty();
    }

    @PatchMapping("/password/edit")
    public DataResponse<Object> updatePassword(@RequestBody UserPasswordRequest userPasswordRequest, Authentication authentication) {
        String username = authentication.getName();
        userService.editPassword(username, userPasswordRequest.getPassword());

        return DataResponse.empty();
    }
}
