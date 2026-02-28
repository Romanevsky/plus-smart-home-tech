package ru.yandex.practicum.telemetry.collector.mapper;

import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.telemetry.collector.model.hub.DeviceAction;

public class DeviceActionMapper {
    public static DeviceActionAvro modelToAvro(DeviceAction action) {
        return DeviceActionAvro.newBuilder()
                .setType(ActionTypeAvro.valueOf(action.getType().toString()))
                .setSensorId(action.getSensorId())
                .setValue(action.getValue())
                .build();
    }
}
