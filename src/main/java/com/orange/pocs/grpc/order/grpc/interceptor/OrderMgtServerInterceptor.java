package com.orange.pocs.grpc.order.grpc.interceptor;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

@GrpcGlobalServerInterceptor
@Slf4j
public class OrderMgtServerInterceptor implements io.grpc.ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        call.setCompression("gzip");
        log.info("======= [Server Interceptor] : Remote Method Invoked - " + call.getMethodDescriptor().getFullMethodName());
        ServerCall<ReqT, RespT> serverCall = new OrderMgtServerCall<>(call);
        return new OrderMgtServerCallListener<>(next.startCall(serverCall, headers));
    }

}