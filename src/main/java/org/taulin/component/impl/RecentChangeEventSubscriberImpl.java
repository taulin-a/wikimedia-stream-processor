package org.taulin.component.impl;

import cloud.prefab.sse.events.DataEvent;
import cloud.prefab.sse.events.Event;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import lombok.extern.slf4j.Slf4j;
import org.taulin.serialization.deserializer.json.RecentChangeEventJsonDeserializer;
import org.taulin.component.RecentChangeEventProducer;
import org.taulin.model.RecentChangeEventDTO;

import java.util.Optional;
import java.util.concurrent.Flow;

@Slf4j
public class RecentChangeEventSubscriberImpl implements Flow.Subscriber<Event> {
    private final RecentChangeEventJsonDeserializer deserializer;
    private final RecentChangeEventProducer recentChangeEventProducer;
    private final Integer batchSize;
    private final Long sleepBetweenBatches;
    private Integer currentEvent = 0;

    @Inject
    public RecentChangeEventSubscriberImpl(RecentChangeEventJsonDeserializer deserializer,
                                           RecentChangeEventProducer recentChangeEventProducer,
                                           @Named("wikimedia.subscriber.batch.size") Integer batchSize,
                                           @Named("wikimedia.subscriber.sleep.between.batches") String sleepBetweenBatchesStr) {
        this.deserializer = deserializer;
        this.recentChangeEventProducer = recentChangeEventProducer;
        this.batchSize = batchSize;
        this.sleepBetweenBatches = Long.valueOf(sleepBetweenBatchesStr);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        log.info("Subscribed to new event source");
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Event event) {
        if (!(event instanceof DataEvent dataEvent)) return;
        Optional<RecentChangeEventDTO> recentChangeEvent = deserializer.deserialize(dataEvent.getData());
        if (recentChangeEvent.isPresent()) {
            log.info("New recent change event received: {}", recentChangeEvent);
            recentChangeEventProducer.send(recentChangeEvent.get());
        }

        checkBatchSize();
    }

    private void addCurrentEvent() {
        currentEvent++;
    }

    private void resetCurrentEvent() {
        currentEvent = 0;
    }

    private void sleep() {
        try {
            Thread.sleep(sleepBetweenBatches);
        } catch (InterruptedException e) {
            log.error("Failed while sleeping between batches");
        }
    }

    private void checkBatchSize() {
        addCurrentEvent();

        if (currentEvent >= batchSize) {
            sleep();
            resetCurrentEvent();
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
