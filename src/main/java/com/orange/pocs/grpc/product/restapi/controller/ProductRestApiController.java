package com.orange.pocs.grpc.product.restapi.controller;

import com.orange.pocs.grpc.product.restapi.model.ProductIdResponseRequest;
import com.orange.pocs.grpc.product.restapi.model.ProductRequest;
import com.orange.pocs.grpc.product.restapi.service.ProductRestServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/product")
@RestController
@Slf4j
public class ProductRestApiController {

    @Autowired
    private ProductRestServiceImpl productRestService;

    @PostMapping
    public ResponseEntity<ProductIdResponseRequest> addProduct(@RequestBody ProductRequest request) {
        log.info("Add product {}", request);
        ProductIdResponseRequest response = productRestService.addProduct(request);
        log.info("Product added {}", response.getValue());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductRequest> getProduct(@PathVariable String id) {
        log.info("Get product {}", id);
        ProductRequest response = productRestService.getProduct(new ProductIdResponseRequest(id));
        log.info("Product {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
