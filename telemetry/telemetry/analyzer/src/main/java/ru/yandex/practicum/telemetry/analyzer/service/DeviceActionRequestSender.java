package ru.yandex.practicum.telemetry.analyzer.service;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;

@Service
public class DeviceActionRequestSender {
    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    public DeviceActionRequestSender(@GrpcClient("hub-router") HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient) {
        this.hubRouterClient = hubRouterClient;
    }

    public void send(DeviceActionRequest action) {
        hubRouterClient.handleDeviceAction(action);
    }
}
