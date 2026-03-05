package ru.yandex.practicum.telemetry.collector.service.sensor;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.telemetry.collector.config.CollectorClient;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;

@RequiredArgsConstructor
public abstract class SensorEventHandler {
    protected final String topic;
    protected final CollectorClient client;

    public abstract void handle(SensorEvent event);

    public abstract SensorEventType getEventType();
}
