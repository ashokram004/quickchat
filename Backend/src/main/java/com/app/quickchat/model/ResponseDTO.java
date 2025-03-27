package com.app.quickchat.model;

import lombok.Getter;

@Getter
public class ResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;

    public ResponseDTO(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

}
