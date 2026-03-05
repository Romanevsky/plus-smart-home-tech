package ru.yandex.practicum.telemetry.collector.service.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.collector.config.CollectorClient;
import ru.yandex.practicum.telemetry.collector.model.hub.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.hub.HubEventType;

@Component
public class DeviceAddedEventHandler extends HubEventHandler {

    @Autowired
    public DeviceAddedEventHandler(@Value("${collector.kafka.topic.hubs}") String topic,
                                   CollectorClient client) {
        super(topic, client);
    }

    @Override
    public HubEventType getEventType() {
        return HubEventType.DEVICE_ADDED;
    }

    @Override
    public void handle(HubEvent event) {
        DeviceAddedEvent deviceAddedEvent = (DeviceAddedEvent) event;

        DeviceAddedEventAvro data = DeviceAddedEventAvro.newBuilder()
                .setId(deviceAddedEvent.getId())
                .setType(DeviceTypeAvro.valueOf(deviceAddedEvent.getDeviceType().toString()))
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
