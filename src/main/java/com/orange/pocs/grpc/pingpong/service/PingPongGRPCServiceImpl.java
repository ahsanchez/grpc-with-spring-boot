package com.orange.pocs.grpc.pingpong.service;


import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@Slf4j
public class PingPongGRPCServiceImpl extends PingPongServiceGrpc.PingPongServiceImplBase {
    @Override
    public void ping(PingRequest request, StreamObserver<PongResponse> responseObserver) {
        log.info("ping");
        PongResponse response = PongResponse.newBuilder().setPong("pong").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        log.info("pong");
    }
}