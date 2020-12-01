package com.orange.pocs.grpc.product.restapi.model;

import lombok.Data;

@Data
public class ProductRequest {
    private String id;
    private String name;
    private String description;
    private long price;
}
