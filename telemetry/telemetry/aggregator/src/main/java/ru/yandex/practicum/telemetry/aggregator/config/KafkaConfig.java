package ru.yandex.practicum.telemetry.aggregator.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Properties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties("aggregator.kafka")
public class KafkaConfig {

    private final Duration closeTimeout;
    private final ProducerConfig producer;
    private final ConsumerConfig consumer;

    @Setter
    @Getter
    public static class ProducerConfig {
        private String topic;
        private Properties properties;
    }

    @Setter
    @Getter
    public static class ConsumerConfig {
        private String topic;
        private Properties properties;
        private Duration pollTimeout;
    }
}
