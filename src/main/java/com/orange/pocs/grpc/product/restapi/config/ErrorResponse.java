package com.orange.pocs.grpc.product.restapi.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private int code;
    private String message;
    private String date;
}