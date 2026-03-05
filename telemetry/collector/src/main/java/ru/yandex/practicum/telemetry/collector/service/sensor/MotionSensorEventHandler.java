package ru.yandex.practicum.telemetry.collector.service.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.config.CollectorClient;
import ru.yandex.practicum.telemetry.collector.model.sensor.MotionSensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensor.SensorEventType;

@Component
public class MotionSensorEventHandler extends SensorEventHandler {

    @Autowired
    public MotionSensorEventHandler(@Value("${collector.kafka.topic.sensors}") String topic,
                                    CollectorClient client) {
        super(topic, client);
    }

    @Override
    public SensorEventType getEventType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEvent event) {
        MotionSensorEvent motionEvent = (MotionSensorEvent) event;

        MotionSensorAvro data = MotionSensorAvro.newBuilder()
                .setMotion(motionEvent.isMotion())
                .setLinkQuality(motionEvent.getLinkQuality())
                .setVoltage(motionEvent.getVoltage())
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
