package ru.yandex.practicum.telemetry.analyzer.service;

import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.config.KafkaConfig;
import ru.yandex.practicum.telemetry.analyzer.model.Action;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;
import ru.yandex.practicum.telemetry.analyzer.model.ConditionOperation;
import ru.yandex.practicum.telemetry.analyzer.model.ConditionType;
import ru.yandex.practicum.telemetry.analyzer.storage.ScenarioRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Service
@Slf4j
public class SnapshotProcessor implements Runnable {

    private final KafkaConfig.ConsumerConfig config;
    private final Duration closeTimeout;
    private final DeviceActionRequestSender requestSender;
    private final ScenarioRepository scenarioRepository;

    private Consumer<Void, SpecificRecordBase> consumer;

    @Autowired
    public SnapshotProcessor(KafkaConfig kafkaConfig,
                             DeviceActionRequestSender requestSender,
                             ScenarioRepository scenarioRepository) {
        config = kafkaConfig.getConsumers().get(getClass().getSimpleName());
        closeTimeout = kafkaConfig.getCloseTimeout();
        this.requestSender = requestSender;
        this.scenarioRepository = scenarioRepository;
    }

    @Override
    public void run() {
        initConsumer();
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
        try {
            consumer.subscribe(config.getTopics());
            while (true) {
                ConsumerRecords<Void, SpecificRecordBase> records = consumer.poll(config.getPollTimeout());
                for (ConsumerRecord<Void, SpecificRecordBase> record : records) {
                    // обрабатываем очередную запись
                    handleRecord(record);
                }
                consumer.commitSync();
            }
        } catch (WakeupException ignored) {
            // Do nothing
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от хаба", e);
        } finally {
            try {
                consumer.commitSync();
                consumer.close(closeTimeout);
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
            }
        }
    }

    private void initConsumer() {
        consumer = new KafkaConsumer<>(config.getProperties());
    }

    @Transactional(readOnly = true)
    private void handleRecord(ConsumerRecord<Void, SpecificRecordBase> record) {
        SensorsSnapshotAvro sensorSnapshot = (SensorsSnapshotAvro) record.value();
        Map<String, SensorStateAvro> sensorsState = sensorSnapshot.getSensorsState();
        String hubId = sensorSnapshot.getHubId();
        scenarioRepository.findByHubId(hubId).stream()
                .filter(scenario -> checkConditions(scenario.getConditions(), sensorsState))
                .forEach(scenario -> sendActions(scenario.getActions(), hubId, scenario.getName()));
    }

    private void sendActions(Map<String, Action> actions, String hubId, String scenarioName) {
        Instant now = Instant.now();
        actions.entrySet().stream()
                .map(action -> {
                    DeviceActionProto actionProto = DeviceActionProto.newBuilder()
                            .setSensorId(action.getKey())
                            .setType(ActionTypeProto.valueOf(action.getValue().getType().toString()))
                            .setValue(action.getValue().getValue())
                            .build();
                    return DeviceActionRequest.newBuilder()
                            .setHubId(hubId)
                            .setScenarioName(scenarioName)
                            .setAction(actionProto)
                            .setTimestamp(Timestamp.newBuilder().setSeconds(now.getEpochSecond()).setNanos(now.getNano()).build())
                            .build();
                }).forEach(requestSender::send);
    }

    private boolean checkConditions(Map<String, Condition> conditions, Map<String, SensorStateAvro> sensorState) {
        return conditions.entrySet().stream()
                .allMatch(entry -> {
                    SensorStateAvro state = sensorState.get(entry.getKey());
                    if (state == null) {
                        return false;
                    }
                    return checkCondition(entry.getValue(), state);
                });
    }

    private boolean checkCondition(Condition condition, SensorStateAvro sensorState) {
        ConditionOperation operation = ConditionOperation.valueOf(condition.getOperation().toString());
        Integer conditionValue = condition.getValue();
        ConditionType type = condition.getType();
        return switch (sensorState.getData()) {
            case ClimateSensorAvro climate -> switch (type) {
                case TEMPERATURE -> checkOperationCondition(operation, conditionValue, climate.getTemperatureC());
                case CO2LEVEL -> checkOperationCondition(operation, conditionValue, climate.getCo2Level());
                case HUMIDITY -> checkOperationCondition(operation, conditionValue, climate.getHumidity());
                default -> false;
            };

            case LightSensorAvro light -> switch (type) {
                case LUMINOSITY -> checkOperationCondition(operation, conditionValue, light.getLuminosity());
                default -> false;
            };

            case MotionSensorAvro motion -> switch (type) {
                case MOTION -> motion.getMotion() == conditionValue.equals(1);
                default -> false;
            };

            case SwitchSensorAvro switchSensor -> switch (type) {
                case SWITCH -> switchSensor.getState() == conditionValue.equals(1);
                default -> false;
            };

            case TemperatureSensorAvro temperature -> switch (type) {
                case TEMPERATURE -> checkOperationCondition(operation, conditionValue, temperature.getTemperatureC());
                default -> false;
            };

            default -> throw new IllegalStateException("Unexpected value: " + sensorState.getData());
        };
    }

    private boolean checkOperationCondition(ConditionOperation operation, Integer conditionValue, Integer value) {
        return switch (operation) {
            case EQUALS -> value.equals(conditionValue);
            case GREATER_THAN -> value > conditionValue;
            case LOWER_THAN -> value < conditionValue;
            case null -> throw new IllegalStateException("Unexpected value: " + operation);
        };
    }
}
