package com.skillspace.user.util;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomResponse<T> {
    private String message;
    private int statusCode;
    private T data;

    public CustomResponse(String message, int statusCode, T data) {
        this.message = message;
        this.statusCode = statusCode;
        this.data = data;
    }

    public CustomResponse(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

}

