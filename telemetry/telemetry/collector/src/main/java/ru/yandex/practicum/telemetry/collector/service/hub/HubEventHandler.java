package ru.yandex.practicum.telemetry.collector.service.hub;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.telemetry.collector.config.CollectorClient;

@RequiredArgsConstructor
public abstract class HubEventHandler {
    protected final String topic;
    protected final CollectorClient client;

    public abstract void handle(HubEventProto event);

    public abstract HubEventProto.PayloadCase getEventType();
}
