package com.orange.pocs.grpc.product.restapi.service;


import com.orange.pocs.grpc.product.restapi.exception.NotFoundException;
import com.orange.pocs.grpc.product.restapi.model.ProductIdResponseRequest;
import com.orange.pocs.grpc.product.restapi.model.ProductRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class ProductRestServiceImpl {

    private Map<String, ProductRequest> productMap;

    public ProductIdResponseRequest addProduct(ProductRequest request) {
        if (productMap == null) {
            productMap = new HashMap<>();
            productMap.put("test", request);
        }
        String uuid = UUID.randomUUID().toString();
        productMap.put(uuid, request);
        return new ProductIdResponseRequest(uuid);
    }


    public ProductRequest getProduct(ProductIdResponseRequest request) {
        String id = request.getValue();
        if (productMap != null && productMap.containsKey(id)) {
            return productMap.get(id);
        }
        throw new NotFoundException("Product " + request.getValue() + " not found");
    }
}