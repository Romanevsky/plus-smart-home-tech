package ru.yandex.practicum.telemetry.collector.config;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CollectorClientConfig {

    private final KafkaConfig config;

    @Bean
    public CollectorClient collectorClient() {
        return new CollectorClient() {

            private Producer<Void, SpecificRecordBase> producer;

            @Override
            public Producer<Void, SpecificRecordBase> getProducer() {
                if (producer == null) {
                    initProducer();
                }
                return producer;
            }

            @PreDestroy
            @Override
            public void stop() {
                producer.flush();
                producer.close(config.getCloseTimeout());
            }

            private void initProducer() {
                producer = new KafkaProducer<>(config.getProperties());
            }
        };
    }
}
