package ru.yandex.practicum.telemetry.collector.config;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;

public interface CollectorClient {
    Producer<Void, SpecificRecordBase> getProducer();

    void stop();
}
