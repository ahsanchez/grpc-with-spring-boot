package com.orange.pocs.grpc.product.grpc.service;


import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@GrpcService
@Slf4j
public class ProductGRPCServiceImpl extends ProductServiceGrpc.ProductServiceImplBase {

    private Map<String, Product> productMap;

    @Override
    public void addProduct(Product request, StreamObserver<ProductId> responseObserver) {
        log.info("Add product {}", request);
        if (productMap == null) {
            productMap = new HashMap<>();
        }
        String uuid = UUID.randomUUID().toString();
        productMap.put(uuid, request);
        ProductId id = ProductId.newBuilder().setValue(uuid).build();
        responseObserver.onNext(id);
        responseObserver.onCompleted();
        log.info("Product added {}", productMap.get(uuid));
    }

    @Override
    public void getProduct(ProductId request, StreamObserver<Product> responseObserver) {
        log.info("Get product {}", request.getValue());
        String id = request.getValue();
        if (productMap != null && productMap.containsKey(id)) {
            responseObserver.onNext(productMap.get(id));
            responseObserver.onCompleted();
            log.info("Product={}", productMap.get(id));
        } else {
            log.info("Product {} not found", request.getValue());
            responseObserver.onError(new StatusException(Status.NOT_FOUND));
        }
    }

}