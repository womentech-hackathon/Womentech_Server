package com.womentech.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {
    @GetMapping("/")
    public String hello() {
        return "쑥스러운 자매들";
    }

    @GetMapping("/hello")
    @Operation(summary = "인사 API", description = "이름에 따라 인사말을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 인사말을 반환합니다.",
            content = @Content(mediaType = "text/plain",
                    schema = @Schema(implementation = String.class)))
    public String hello(@RequestParam(defaultValue = "Guest") @Parameter(description = "이름") String name) {
        return "Hello, " + name + "!";
    }
}
