package ru.yandex.practicum.telemetry.collector.mapper;

import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.telemetry.collector.model.hub.ScenarioCondition;

public class ScenarioConditionMapper {
    public static ScenarioConditionAvro modelToAvro(ScenarioCondition condition) {
        return ScenarioConditionAvro.newBuilder()
                .setType(ConditionTypeAvro.valueOf(condition.getType().toString()))
                .setValue(condition.getValue())
                .setSensorId(condition.getSensorId())
                .setOperation(ConditionOperationAvro.valueOf(condition.getOperation().toString()))
                .build();
    }
}
