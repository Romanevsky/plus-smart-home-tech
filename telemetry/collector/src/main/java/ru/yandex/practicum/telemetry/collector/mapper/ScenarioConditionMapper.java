package ru.yandex.practicum.telemetry.collector.mapper;

import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

public class ScenarioConditionMapper {
    public static ScenarioConditionAvro protoToAvro(ScenarioConditionProto condition) {
        return ScenarioConditionAvro.newBuilder()
                .setType(ConditionTypeAvro.valueOf(condition.getType().toString()))
                .setValue(getOneOfValue(condition))
                .setSensorId(condition.getSensorId())
                .setOperation(ConditionOperationAvro.valueOf(condition.getOperation().toString()))
                .build();
    }

    private static Object getOneOfValue(ScenarioConditionProto condition) {
        return switch (condition.getValueCase()) {
            case INT_VALUE -> condition.getIntValue();
            case BOOL_VALUE -> condition.getBoolValue();
            case VALUE_NOT_SET -> null;
        };
    }
}
