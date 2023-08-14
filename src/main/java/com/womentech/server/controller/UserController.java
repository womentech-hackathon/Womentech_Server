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
    @Operation(summary = "로그아웃", description = "로그아웃합니다.")
    public DataResponse<Object> logout(@RequestHeader("Authorization") String authorization, Authentication authentication) {
        String username = authentication.getName();
        String token = authorization.split(" ")[1];
        userService.logout(username, token);

        return DataResponse.empty();
    }

    @DeleteMapping("/withdrawal")
    @Operation(summary = "회원탈퇴", description = "회원을 탈퇴합니다.")
    public DataResponse<Object> deleteUser(Authentication authentication) {
        String username = authentication.getName();
        userService.deleteUser(username);

        return DataResponse.empty();
    }

    @GetMapping("/name")
    @Operation(summary = "이름 조회", description = "이름을 조회합니다.")
    public DataResponse<Object> getUserName(Authentication authentication) {
        String username = authentication.getName();

        return DataResponse.of(userService.getName(username));
    }

    @PatchMapping("/name/edit")
    @Operation(summary = "이름 변경", description = "이름을 변경합니다.")
    public DataResponse<Object> updateUserName(@RequestBody UserNameRequest userNameRequest, Authentication authentication) {
        String username = authentication.getName();
        userService.editName(username, userNameRequest.getName());

        return DataResponse.empty();
    }

    @PatchMapping("/password/edit")
    @Operation(summary = "비밀번호 변경", description = "비밀번호를 변경합니다.")
    public DataResponse<Object> updateUserPassword(@RequestBody UserPasswordRequest userPasswordRequest, Authentication authentication) {
        String username = authentication.getName();
        userService.editPassword(username, userPasswordRequest.getPassword());

        return DataResponse.empty();
    }
}
