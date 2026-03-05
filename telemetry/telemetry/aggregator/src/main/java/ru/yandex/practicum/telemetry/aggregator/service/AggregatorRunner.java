package ru.yandex.practicum.telemetry.aggregator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.aggregator.config.KafkaConfig;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Component
@RequiredArgsConstructor
public class AggregatorRunner implements CommandLineRunner {

    private final KafkaConfig config;
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();
    private Producer<Void, SpecificRecordBase> producer;
    private Consumer<Void, SpecificRecordBase> consumer;

    @Override
    public void run(String... args) throws Exception {
        initConsumer();
        initProducer();
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
        try {
            consumer.subscribe(List.of(config.getConsumer().getTopic()));
            while (true) {
                ConsumerRecords<Void, SpecificRecordBase> records = consumer.poll(config.getConsumer().getPollTimeout());
                for (ConsumerRecord<Void, SpecificRecordBase> record : records) {
                    // обрабатываем очередную запись
                    updateState(record).ifPresent(value ->
                            producer.send(new ProducerRecord<>(config.getProducer().getTopic(), value)));
                }
                consumer.commitSync();
            }
        } catch (WakeupException ignored) {
            // Do nothing
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                producer.flush();
                producer.close(config.getCloseTimeout());
                consumer.commitSync();
                consumer.close(config.getCloseTimeout());
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
            }
        }
    }

    private void initProducer() {
        producer = new KafkaProducer<>(config.getProducer().getProperties());
    }

    private void initConsumer() {
        consumer = new KafkaConsumer<>(config.getConsumer().getProperties());
    }

    private Optional<SensorsSnapshotAvro> updateState(ConsumerRecord<Void, SpecificRecordBase> record) {
        SensorEventAvro event = (SensorEventAvro) record.value();
        SensorsSnapshotAvro snapshot = snapshots.get(event.getHubId());
        if (snapshot == null) {
            snapshot = SensorsSnapshotAvro.newBuilder()
                    .setHubId(event.getHubId())
                    .setTimestamp(Instant.now())
                    .setSensorsState(new HashMap<>())
                    .build();
            snapshots.put(event.getHubId(), snapshot);
        }

        SensorStateAvro oldState = snapshot.getSensorsState().get(event.getId());
        if (oldState != null
                && (oldState.getTimestamp().isAfter(event.getTimestamp()) || oldState.getData().equals(event.getPayload()))) {
            return Optional.empty();
        }

        SensorStateAvro newState = SensorStateAvro.newBuilder()
                .setData(event.getPayload())
                .setTimestamp(event.getTimestamp())
                .build();
        snapshot.getSensorsState().put(event.getId(), newState);
        snapshot.setTimestamp(event.getTimestamp());
        return Optional.of(snapshot);
    }
}
