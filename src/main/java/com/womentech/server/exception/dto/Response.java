package com.womentech.server.exception.dto;

import com.womentech.server.exception.Code;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class Response {
    private final Boolean success;
    private final Integer code;
    private final String message;

    public static Response of(Boolean success, Code code) {
        return new Response(success, code.getCode(), code.getMessage());
    }

    public static Response of(Boolean success, Code errorCode, Exception e) {
        return new Response(success, errorCode.getCode(), errorCode.getMessage(e));
    }

    public static Response of(Boolean success, Code errorCode, String message) {
        return new Response(success, errorCode.getCode(), errorCode.getMessage(message));
    }
}
