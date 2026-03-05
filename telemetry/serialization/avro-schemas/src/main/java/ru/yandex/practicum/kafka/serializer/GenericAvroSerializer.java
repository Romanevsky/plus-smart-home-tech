package ru.yandex.practicum.kafka.serializer;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class GenericAvroSerializer implements Serializer<SpecificRecordBase> {
    private final EncoderFactory encoderFactory = EncoderFactory.get();
    private BinaryEncoder encoder;

    public byte[] serialize(String topic, SpecificRecordBase data) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            if (data != null) {
                DatumWriter<SpecificRecordBase> writer = new SpecificDatumWriter<>(data.getSchema());
                encoder = encoderFactory.binaryEncoder(out, encoder);
                writer.write(data, encoder);
                encoder.flush();
            }
            return out.toByteArray();
        } catch (IOException ex) {
            throw new SerializationException("Ошибка сериализация данных для топика [" + topic + "]", ex);
        }
    }
}
