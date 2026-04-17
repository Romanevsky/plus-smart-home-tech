package ru.yandex.practicum.telemetry.analyzer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@ConfigurationProperties("analyzer.kafka")
public class KafkaConfig {

    private final Duration closeTimeout;
    private final Map<String, ConsumerConfig> consumers;

    public KafkaConfig(Duration closeTimeout, Map<String, String> commonProperties, List<ConsumerConfig> consumers) {
        this.closeTimeout = closeTimeout;
        this.consumers = consumers.stream()
                .peek(config -> {
                    Properties merged = new Properties();
                    merged.putAll(commonProperties);
                    merged.putAll(config.getProperties());
                    config.setProperties(merged);
                })
                .collect(Collectors.toMap(ConsumerConfig::getType, Function.identity()));
    }

    @Getter
    @Setter
    public static class ConsumerConfig {
        private String type;
        private Properties properties;
        private List<String> topics;
        private Duration pollTimeout;
    }
}
