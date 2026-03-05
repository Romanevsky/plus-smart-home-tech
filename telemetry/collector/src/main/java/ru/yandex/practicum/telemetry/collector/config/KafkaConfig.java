package ru.yandex.practicum.telemetry.collector.config;

import jakarta.annotation.PreDestroy;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.VoidSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.serializer.GenericAvroSerializer;

import java.time.Duration;
import java.util.Properties;

@Configuration
public class KafkaConfig {

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
                producer.close(Duration.ofSeconds(10));
            }

            private void initProducer() {
                Properties config = new Properties();
                config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
                config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, VoidSerializer.class);
                config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GenericAvroSerializer.class);

                producer = new KafkaProducer<>(config);
            }
        };
    }
}
