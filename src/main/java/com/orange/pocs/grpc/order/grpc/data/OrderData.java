package com.orange.pocs.grpc.order.grpc.data;

import com.orange.pocs.grpc.order.grpc.service.OrderServiceOuterClass;

public class OrderData {

    public static OrderServiceOuterClass.Order ord1 = OrderServiceOuterClass.Order.newBuilder()
            .setId("102")
            .addItems("Google Pixel 3A").addItems("Mac Book Pro")
            .setDestination("Mountain View, CA")
            .setPrice(1800)
            .build();
    public static OrderServiceOuterClass.Order ord2 = OrderServiceOuterClass.Order.newBuilder()
            .setId("103")
            .addItems("Apple Watch S4")
            .setDestination("San Jose, CA")
            .setPrice(400)
            .build();
    public static OrderServiceOuterClass.Order ord3 = OrderServiceOuterClass.Order.newBuilder()
            .setId("104")
            .addItems("Google Home Mini").addItems("Google Nest Hub")
            .setDestination("Mountain View, CA")
            .setPrice(400)
            .build();
    public static OrderServiceOuterClass.Order ord4 = OrderServiceOuterClass.Order.newBuilder()
            .setId("105")
            .addItems("Amazon Echo")
            .setDestination("San Jose, CA")
            .setPrice(30)
            .build();
    public static OrderServiceOuterClass.Order ord5 = OrderServiceOuterClass.Order.newBuilder()
            .setId("106")
            .addItems("Amazon Echo").addItems("Apple iPhone XS")
            .setDestination("Mountain View, CA")
            .setPrice(300)
            .build();


}
