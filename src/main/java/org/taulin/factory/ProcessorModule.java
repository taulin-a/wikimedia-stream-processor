package org.taulin.factory;

import cloud.prefab.sse.events.Event;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import lombok.extern.slf4j.Slf4j;
import org.taulin.component.EventProcessorWorker;
import org.taulin.component.RecentChangeEventDeserializer;
import org.taulin.component.RecentChangeEventProducer;
import org.taulin.component.impl.RecentChangeEventDeserializerImpl;
import org.taulin.component.impl.EventProcessorWorkerImpl;
import org.taulin.component.impl.RecentChangeEventProducerImpl;
import org.taulin.component.impl.RecentChangeEventSubscriberImpl;
import org.taulin.exception.ConfigurationException;
import org.taulin.util.ResourceLoaderUtil;

import java.io.FileReader;
import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Properties;
import java.util.concurrent.Flow;

@Slf4j
public final class ProcessorModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(EventProcessorWorker.class).to(EventProcessorWorkerImpl.class);
        bind(HttpClient.class).toInstance(buildHttpClient());
        bind(new TypeLiteral<Flow.Subscriber<Event>>() {}).to(RecentChangeEventSubscriberImpl.class);
        bind(ObjectMapper.class).toInstance(new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));
        bind(RecentChangeEventDeserializer.class).to(RecentChangeEventDeserializerImpl.class);
        bind(RecentChangeEventProducer.class).to(RecentChangeEventProducerImpl.class);
        Names.bindProperties(binder(), loadApplicationProperties());
    }

    private HttpClient buildHttpClient() {
        return HttpClient.newBuilder()
                .build();
    }

    private Properties loadApplicationProperties() {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(ResourceLoaderUtil.loadResource("application.properties")));
            return properties;
        } catch (IOException ex) {
            log.error("Unable to load application.properties.");
            throw new ConfigurationException("Unable to load application properties configuration", ex);
        }
    }
}
