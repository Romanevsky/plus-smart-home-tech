package ru.yandex.practicum.telemetry.collector.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Properties;

@RequiredArgsConstructor
@Getter
@ConfigurationProperties("collector.kafka")
public class KafkaConfig {

    private final Duration closeTimeout;
    private final Properties properties;
}
