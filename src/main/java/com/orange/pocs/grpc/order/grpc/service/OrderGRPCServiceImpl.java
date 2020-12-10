package com.orange.pocs.grpc.order.grpc.service;


import com.google.protobuf.StringValue;
import com.orange.pocs.grpc.order.grpc.data.OrderData;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@GrpcService
@Slf4j
public class OrderGRPCServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {

    private Map<String, OrderServiceOuterClass.CombinedShipment> combinedShipmentMap = new HashMap<>();

    public static final int BATCH_SIZE = 3;

    private Map<String, OrderServiceOuterClass.Order> orderMap = Stream.of(
            new AbstractMap.SimpleEntry<>(OrderData.ord1.getId(), OrderData.ord1),
            new AbstractMap.SimpleEntry<>(OrderData.ord2.getId(), OrderData.ord2),
            new AbstractMap.SimpleEntry<>(OrderData.ord3.getId(), OrderData.ord3),
            new AbstractMap.SimpleEntry<>(OrderData.ord4.getId(), OrderData.ord4),
            new AbstractMap.SimpleEntry<>(OrderData.ord5.getId(), OrderData.ord5))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    // Unary
    @Override
    public void getOrder(StringValue request, StreamObserver<OrderServiceOuterClass.Order> responseObserver) {
        if (request.getValue().equals("1000")) {
            responseObserver.onError(new StatusException(Status.INVALID_ARGUMENT));
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        OrderServiceOuterClass.Order order = orderMap.get(request.getValue());
        if (order != null) {
            responseObserver.onNext(order);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new StatusException(Status.NOT_FOUND));
        }
    }

    @Override
    public void searchOrders(StringValue request, StreamObserver<OrderServiceOuterClass.Order> responseObserver) {
        for (Map.Entry<String, OrderServiceOuterClass.Order> orderEntry : orderMap.entrySet()) {
            OrderServiceOuterClass.Order order = orderEntry.getValue();
            int itemsCount = order.getItemsCount();
            for (int index = 0; index < itemsCount; index++) {
                String item = order.getItems(index);
                if (item.contains(request.getValue())) {
                    responseObserver.onNext(order);
                    break;
                }
            }
        }
        responseObserver.onCompleted();
    }

    // Client Streaming
    @Override
    public StreamObserver<OrderServiceOuterClass.Order> updateOrders(StreamObserver<StringValue> responseObserver) {
        return new StreamObserver<OrderServiceOuterClass.Order>() {

            StringBuilder updatedOrderStrBuilder = new StringBuilder().append("Updated Order IDs : ");

            @Override
            public void onNext(OrderServiceOuterClass.Order value) {
                if (value != null) {
                    orderMap.put(value.getId(), value);
                    updatedOrderStrBuilder.append(value.getId()).append(", ");
                }
            }

            @Override
            public void onError(Throwable t) {
                updatedOrderStrBuilder.append(t.getMessage()).append(", ");
            }

            @Override
            public void onCompleted() {
                StringValue updatedOrders = StringValue.newBuilder().setValue(updatedOrderStrBuilder.toString()).build();
                responseObserver.onNext(updatedOrders);
                responseObserver.onCompleted();
            }
        };
    }

    // Bi-di Streaming
    @Override
    public StreamObserver<StringValue> processOrders(StreamObserver<OrderServiceOuterClass.CombinedShipment> responseObserver) {

        return new StreamObserver<StringValue>() {
            int batchMarker = 0;

            @Override
            public void onNext(StringValue value) {
                log.info("Order Proc : ID - " + value.getValue());
                OrderServiceOuterClass.Order currentOrder = orderMap.get(value.getValue());
                if (currentOrder == null) {
                    log.info("No order found. ID - " + value.getValue());
                    return;
                }
                // Processing an order and increment batch marker to
                batchMarker++;
                String orderDestination = currentOrder.getDestination();
                OrderServiceOuterClass.CombinedShipment existingShipment = combinedShipmentMap.get(orderDestination);

                if (existingShipment != null) {
                    existingShipment = OrderServiceOuterClass.CombinedShipment.newBuilder(existingShipment).addOrdersList(currentOrder).build();
                    combinedShipmentMap.put(orderDestination, existingShipment);
                } else {
                    OrderServiceOuterClass.CombinedShipment shipment = OrderServiceOuterClass.CombinedShipment.newBuilder().build();
                    shipment = shipment.newBuilderForType()
                            .addOrdersList(currentOrder)
                            .setId("CMB-" + new Random().nextInt(1000) + ":" + currentOrder.getDestination())
                            .setStatus("Processed!")
                            .build();
                    combinedShipmentMap.put(currentOrder.getDestination(), shipment);
                }

                if (batchMarker == BATCH_SIZE) {
                    // Order batch completed. Flush all existing shipments.
                    for (Map.Entry<String, OrderServiceOuterClass.CombinedShipment> entry : combinedShipmentMap.entrySet()) {
                        responseObserver.onNext(entry.getValue());
                    }
                    // Reset batch marker
                    batchMarker = 0;
                    combinedShipmentMap.clear();
                }
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                for (Map.Entry<String, OrderServiceOuterClass.CombinedShipment> entry : combinedShipmentMap.entrySet()) {
                    responseObserver.onNext(entry.getValue());
                }
                responseObserver.onCompleted();
            }

        };
    }


}