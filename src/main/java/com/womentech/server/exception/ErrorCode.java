package com.womentech.server.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    IDENTIFIER_DUPLICATED(HttpStatus.CONFLICT, "409"),
    IDENTIFIER_NOT_FOUND(HttpStatus.NOT_FOUND, "404"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "401");

    private HttpStatus httpStatus;
    private String message;
}
