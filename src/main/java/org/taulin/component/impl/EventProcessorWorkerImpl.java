package org.taulin.component.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.sse.InboundSseEvent;
import jakarta.ws.rs.sse.SseEventSource;
import lombok.extern.slf4j.Slf4j;
import org.taulin.component.EventProcessorWorker;
import org.taulin.component.RecentChangeEventDeserializer;
import org.taulin.component.RecentChangeEventProducer;
import org.taulin.model.RecentChangeEvent;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class EventProcessorWorkerImpl implements EventProcessorWorker {
    private final String wikimediaEventsUrl;
    private final Client sseClient;
    private final RecentChangeEventDeserializer deserializer;
    private final ScheduledExecutorService executorService;
    private final SseEventSource sseEventSource;
    private final RecentChangeEventProducer recentChangeEventProducer;

    @Inject
    public EventProcessorWorkerImpl(@Named("wikimedia.events.url") String wikimediaEventsUrl, Client sseClient,
                                    RecentChangeEventDeserializer deserializer,
                                    RecentChangeEventProducer recentChangeEventProducer) {
        this.wikimediaEventsUrl = wikimediaEventsUrl;
        this.sseClient = sseClient;
        this.deserializer = deserializer;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.sseEventSource = buildEventSource();
        this.recentChangeEventProducer = recentChangeEventProducer;
    }

    @Override
    public void start() {
        sseEventSource.register(this::onEvent, this::onError, this::onComplete);

        executorService.scheduleWithFixedDelay(() -> processEvents(sseEventSource), 0L, 10L,
                TimeUnit.MILLISECONDS);
    }

    private SseEventSource buildEventSource() {
        return SseEventSource.target(sseClient.target(wikimediaEventsUrl))
                .reconnectingEvery(500, TimeUnit.MILLISECONDS)
                .build();
    }

    private void processEvents(final SseEventSource sseEventSource) {
        if (!sseEventSource.isOpen()) {
            sseEventSource.open();
        }

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            log.error("Error while waiting for events: ", e);
        }
    }

    private void onEvent(InboundSseEvent event) {
        Optional<RecentChangeEvent> recentChangeEvent = deserializer.deserialize(event.readData());
        if (recentChangeEvent.isPresent()) {
            log.info("New recent change event received: {}", recentChangeEvent);
            recentChangeEventProducer.send(recentChangeEvent.get());
        }
    }

    private void onError(Throwable e) {
        log.error("Error occurred while consuming event: ", e);
    }

    private void onComplete() {
        log.info("Done consuming events from server");
    }

    @Override
    public void close() {
        try {
            executorService.shutdown();

            if (!executorService.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                log.info("Executor Service shut down");

                if (sseEventSource.isOpen()) {
                    sseEventSource.close(1000, TimeUnit.MILLISECONDS);
                }
            }
        } catch (InterruptedException e) {
            log.error("Failed to shutdown Executor Service", e);
        }
    }
}
