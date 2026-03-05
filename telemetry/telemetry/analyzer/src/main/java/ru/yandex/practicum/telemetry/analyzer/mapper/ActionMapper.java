package ru.yandex.practicum.telemetry.analyzer.mapper;

import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Action;
import ru.yandex.practicum.telemetry.analyzer.model.ActionType;

public class ActionMapper {
    public static Action avroToModel(DeviceActionAvro avro) {
        return Action.builder()
                .type(ActionType.valueOf(avro.getType().toString()))
                .value(avro.getValue())
                .build();
    }
}
