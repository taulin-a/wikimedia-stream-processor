package org.taulin.component.impl;

import cloud.prefab.sse.events.DataEvent;
import cloud.prefab.sse.events.Event;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.taulin.component.RecentChangeEventDeserializer;
import org.taulin.component.RecentChangeEventProducer;
import org.taulin.model.RecentChangeEvent;

import java.util.Optional;
import java.util.concurrent.Flow;

@Slf4j
public class RecentChangeEventSubscriberImpl implements Flow.Subscriber<Event> {
    private final RecentChangeEventDeserializer deserializer;
    private final RecentChangeEventProducer recentChangeEventProducer;

    @Inject
    public RecentChangeEventSubscriberImpl(RecentChangeEventDeserializer deserializer,
                                           RecentChangeEventProducer recentChangeEventProducer) {
        this.deserializer = deserializer;
        this.recentChangeEventProducer = recentChangeEventProducer;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        log.info("Subscribed to new event source");
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Event event) {
        if (!(event instanceof DataEvent dataEvent)) return;
        Optional<RecentChangeEvent> recentChangeEvent = deserializer.deserialize(dataEvent.getData());
        if (recentChangeEvent.isPresent()) {
            log.info("New recent change event received: {}", recentChangeEvent);
            recentChangeEventProducer.send(recentChangeEvent.get());
        }
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Error occurred while consuming event: ", throwable);
    }

    @Override
    public void onComplete() {
        log.info("Done consuming events from server");
    }
}
