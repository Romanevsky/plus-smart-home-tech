package ru.yandex.practicum.telemetry.analyzer.mapper;

import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;
import ru.yandex.practicum.telemetry.analyzer.model.ConditionOperation;
import ru.yandex.practicum.telemetry.analyzer.model.ConditionType;

public class ConditionMapper {
    public static Condition avroToModel(ScenarioConditionAvro avro) {
        return Condition.builder()
                .type(ConditionType.valueOf(avro.getType().toString()))
                .operation(avro.getOperation() == null ? null : ConditionOperation.valueOf(avro.getOperation().toString()))
                .value(valueToInteger(avro.getValue()))
                .build();
    }

    private static Integer valueToInteger(Object avroValue) {
        return switch (avroValue) {
            case null -> null;
            case Integer avroInt -> avroInt;
            case Boolean avroBool -> avroBool ? 1 : 0;
            default -> throw new IllegalStateException("Unexpected value: " + avroValue);
        };
    }
}
