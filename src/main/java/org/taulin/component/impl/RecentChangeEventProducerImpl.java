package org.taulin.component.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.confluent.kafka.serializers.KafkaJsonSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.rocksdb.CompressionType;
import org.taulin.component.RecentChangeEventProducer;
import org.taulin.model.RecentChangeEvent;

import java.util.Properties;

public class RecentChangeEventProducerImpl implements RecentChangeEventProducer {
    private static final String DEFAULT_ACK_CONFIG = "-1";
    private static final String DEFAULT_IDEMPOTENCE_CONFIG = "true";
    private static final String DEFAULT_LINGER_CONFIG = "20";
    private static final String DEFAULT_BATCH_SIZE = Integer.toString(32 * 1024);

    private final KafkaProducer<Long, RecentChangeEvent> producer;
    private final String topicName;

    @Inject
    public RecentChangeEventProducerImpl(@Named("bootstrap.servers") String bootstrapServers,
                                         @Named("topic.name") String topicName,
                                         @Named("kafka.producer.retries") String retries,
                                         @Named("kafka.producer.delivery.timeout") String deliveryTimeout) {
        final Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSerializer.class.getName());
        properties.setProperty(ProducerConfig.ACKS_CONFIG, DEFAULT_ACK_CONFIG);
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, retries);
        properties.setProperty(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeout);
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, DEFAULT_IDEMPOTENCE_CONFIG);
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, CompressionType.SNAPPY_COMPRESSION.getLibraryName());
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, DEFAULT_LINGER_CONFIG);
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, DEFAULT_BATCH_SIZE);

        producer = new KafkaProducer<>(properties);
        this.topicName = topicName;
    }

    @Override
    public void send(RecentChangeEvent recentChangeEvent) {
        final ProducerRecord<Long, RecentChangeEvent> record = new ProducerRecord<>(topicName, recentChangeEvent.id(),
                recentChangeEvent);
        producer.send(record);
    }
}
