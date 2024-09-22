package com.cap.midsenior.prices_api.adapter.in.rest.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleErrorResponse {
    private final String message;

    public static SimpleErrorResponse serviceUnavailable() {
        return new SimpleErrorResponse("Service is currently unavailable.");
    }
}