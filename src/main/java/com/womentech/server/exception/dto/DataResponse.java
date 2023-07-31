package com.womentech.server.exception.dto;

import com.womentech.server.exception.Code;
import lombok.Getter;

@Getter
public class DataResponse<T> extends Response {

    private final T data;

    private DataResponse(T data) {
        super(true, Code.OK.getCode(), Code.OK.getMessage());
        this.data = data;
    }

    private DataResponse(T data, String message) {
        super(true, Code.OK.getCode(), message);
        this.data = data;
    }

    public static <T> DataResponse<T> of(T data) {
        return new DataResponse<>(data);
    }

    public static <T> DataResponse<T> of(T data, String message) {
        return new DataResponse<>(data, message);
    }

    public static <T> DataResponse<T> empty() {
        return new DataResponse<>(null);
    }
}
