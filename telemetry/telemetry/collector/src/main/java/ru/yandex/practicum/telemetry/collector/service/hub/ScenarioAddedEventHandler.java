package ru.yandex.practicum.telemetry.collector.service.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.telemetry.collector.config.CollectorClient;
import ru.yandex.practicum.telemetry.collector.mapper.DeviceActionMapper;
import ru.yandex.practicum.telemetry.collector.mapper.ScenarioConditionMapper;

import java.time.Instant;

@Component
public class ScenarioAddedEventHandler extends HubEventHandler {

    @Autowired
    public ScenarioAddedEventHandler(@Value("${collector.kafka.topic.hubs}") String topic,
                                     CollectorClient client) {
        super(topic, client);
    }

    @Override
    public HubEventProto.PayloadCase getEventType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    public void handle(HubEventProto event) {
        ScenarioAddedEventProto scenarioAddedEvent = event.getScenarioAdded();

        ScenarioAddedEventAvro data = ScenarioAddedEventAvro.newBuilder()
                .setName(scenarioAddedEvent.getName())
                .setActions(scenarioAddedEvent.getActionList().stream().map(DeviceActionMapper::protoToAvro).toList())
                .setConditions(scenarioAddedEvent.getConditionList().stream().map(ScenarioConditionMapper::protoToAvro).toList())
                .build();

        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos()))
                .setPayload(data)
                .build();

        ProducerRecord<Void, SpecificRecordBase> record = new ProducerRecord<>(topic, eventAvro);
        client.getProducer().send(record);
    }
}
