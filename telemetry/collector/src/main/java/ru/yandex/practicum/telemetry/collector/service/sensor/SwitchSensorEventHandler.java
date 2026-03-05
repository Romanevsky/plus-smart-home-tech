package ru.yandex.practicum.telemetry.collector.service.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.telemetry.collector.config.CollectorClient;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.collector.model.sensor.SwitchSensorEvent;

@Component
public class SwitchSensorEventHandler extends SensorEventHandler {

    @Autowired
    public SwitchSensorEventHandler(@Value("${collector.kafka.topic.sensors}") String topic,
                                    CollectorClient client) {
        super(topic, client);
    }

    @Override
    public SensorEventType getEventType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEvent event) {
        SwitchSensorEvent switchEvent = (SwitchSensorEvent) event;

        SwitchSensorAvro data = SwitchSensorAvro.newBuilder()
                .setState(switchEvent.isState())
                .build();

        SensorEventAvro eventAvro = SensorEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setId(event.getId())
                .setTimestamp(event.getTimestamp())
                .setPayload(data)
                .build();

        ProducerRecord<Void, SpecificRecordBase> record = new ProducerRecord<>(topic, eventAvro);
        client.getProducer().send(record);
    }
}
