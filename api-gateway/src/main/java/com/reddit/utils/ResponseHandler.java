package com.reddit.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    private ResponseHandler() {
        throw new IllegalArgumentException("ResponseHandler is a utility class");
    }

    public static ResponseEntity<Object> generateResponse(Object date, String message, boolean isSuccess, HttpStatus httpStatus) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", date);
        response.put("message", message);
        response.put("isSuccess", isSuccess);
        response.put("timeStamp", new Date().getTime());
        return new ResponseEntity<>(response, httpStatus);
    }
}
