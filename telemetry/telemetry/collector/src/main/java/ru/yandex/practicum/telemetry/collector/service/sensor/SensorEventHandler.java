package ru.yandex.practicum.telemetry.collector.service.sensor;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.config.CollectorClient;

@RequiredArgsConstructor
public abstract class SensorEventHandler {
    protected final String topic;
    protected final CollectorClient client;

    public abstract void handle(SensorEventProto event);

    public abstract SensorEventProto.PayloadCase getEventType();
}
