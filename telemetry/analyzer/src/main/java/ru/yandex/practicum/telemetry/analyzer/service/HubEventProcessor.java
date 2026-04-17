package ru.yandex.practicum.telemetry.analyzer.service;

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
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.config.KafkaConfig;
import ru.yandex.practicum.telemetry.analyzer.mapper.ActionMapper;
import ru.yandex.practicum.telemetry.analyzer.mapper.ConditionMapper;
import ru.yandex.practicum.telemetry.analyzer.model.Action;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;
import ru.yandex.practicum.telemetry.analyzer.model.Scenario;
import ru.yandex.practicum.telemetry.analyzer.model.Sensor;
import ru.yandex.practicum.telemetry.analyzer.storage.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.storage.SensorRepository;

import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HubEventProcessor implements Runnable {

    private final KafkaConfig.ConsumerConfig config;
    private final Duration closeTimeout;
    private final SensorRepository sensorRepository;
    private final ScenarioRepository scenarioRepository;

    private Consumer<Void, SpecificRecordBase> consumer;

    @Autowired
    public HubEventProcessor(KafkaConfig kafkaConfig,
                             SensorRepository sensorRepository,
                             ScenarioRepository scenarioRepository) {
        config = kafkaConfig.getConsumers().get(getClass().getSimpleName());
        closeTimeout = kafkaConfig.getCloseTimeout();
        this.sensorRepository = sensorRepository;
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

    @Transactional
    private void handleRecord(ConsumerRecord<Void, SpecificRecordBase> record) {
        HubEventAvro hubEvent = (HubEventAvro) record.value();
        String hubId = hubEvent.getHubId();
        switch (hubEvent.getPayload()) {
            case DeviceAddedEventAvro addedDevice -> sensorRepository.save(new Sensor(addedDevice.getId(), hubId));
            case DeviceRemovedEventAvro removedDevice -> sensorRepository.deleteById(removedDevice.getId());
            case ScenarioAddedEventAvro addedScenario -> {
                Map<String, Action> actions = addedScenario.getActions().stream()
                        .collect(Collectors.toMap(DeviceActionAvro::getSensorId, ActionMapper::avroToModel));

                Map<String, Condition> conditions = addedScenario.getConditions().stream()
                        .collect(Collectors.toMap(ScenarioConditionAvro::getSensorId, ConditionMapper::avroToModel));

                Scenario savedScenario = Scenario.builder()
                        .hubId(hubId)
                        .id(null)
                        .name(addedScenario.getName())
                        .actions(actions)
                        .conditions(conditions)
                        .build();
                scenarioRepository.save(savedScenario);
            }
            case ScenarioRemovedEventAvro removedScenario ->
                    scenarioRepository.deleteByHubIdAndName(hubId, removedScenario.getName());
            default -> throw new IllegalArgumentException("No handler for " + hubEvent.getPayload().getClass());
        }
    }
}
