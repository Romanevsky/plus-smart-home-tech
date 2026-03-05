package ru.yandex.practicum.telemetry.collector.mapper;

import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;

public class DeviceActionMapper {
    public static DeviceActionAvro protoToAvro(DeviceActionProto action) {
        return DeviceActionAvro.newBuilder()
                .setType(ActionTypeAvro.valueOf(action.getType().toString()))
                .setSensorId(action.getSensorId())
                .setValue(action.getValue())
                .build();
    }
}
