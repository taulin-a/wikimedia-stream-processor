package org.taulin.component.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.confluent.kafka.serializers.KafkaJsonSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.taulin.component.RecentChangeEventProducer;
import org.taulin.model.RecentChangeEvent;

import java.util.Properties;

public class RecentChangeEventProducerImpl implements RecentChangeEventProducer {
    private final KafkaProducer<Long, RecentChangeEvent> producer;
    private final String topicName;

    @Inject
    public RecentChangeEventProducerImpl(@Named("bootstrap.servers") String bootstrapServers,
                                         @Named("topic.name") String topicName) {
        final Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSerializer.class.getName());

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
