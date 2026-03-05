package ru.yandex.practicum.telemetry.collector.service.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.collector.config.CollectorClient;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.hub.ScenarioRemovedEvent;

@Component
public class ScenarioRemovedEventHandler extends HubEventHandler {

    @Autowired
    public ScenarioRemovedEventHandler(@Value("${collector.kafka.topic.hubs}") String topic,
                                       CollectorClient client) {
        super(topic, client);
    }

    @Override
    public HubEventType getEventType() {
        return HubEventType.SCENARIO_REMOVED;
    }

    @Override
    public void handle(HubEvent event) {
        ScenarioRemovedEvent scenarioRemovedEvent = (ScenarioRemovedEvent) event;

        ScenarioRemovedEventAvro data = ScenarioRemovedEventAvro.newBuilder()
                .setName(scenarioRemovedEvent.getName())
                .build();

        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(data)
                .build();

        ProducerRecord<Void, SpecificRecordBase> record = new ProducerRecord<>(topic, eventAvro);
        client.getProducer().send(record);
    }
}
