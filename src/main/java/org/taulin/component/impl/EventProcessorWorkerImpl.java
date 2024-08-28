package org.taulin.component.impl;

import cloud.prefab.sse.SSEHandler;
import cloud.prefab.sse.events.Event;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import lombok.extern.slf4j.Slf4j;
import org.taulin.component.EventProcessorWorker;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static cloud.prefab.sse.SSEHandler.EVENT_STREAM_MEDIA_TYPE;

@Slf4j
public class EventProcessorWorkerImpl implements EventProcessorWorker {
    private final String wikimediaEventsUrl;
    private final ScheduledExecutorService executorService;
    private final SSEHandler sseHandler;
    private final Flow.Subscriber<Event> recentChangeEventSubscriber;
    private final HttpClient httpClient;

    @Inject
    public EventProcessorWorkerImpl(@Named("wikimedia.events.url") String wikimediaEventsUrl,
                                    Flow.Subscriber<Event> recentChangeEventSubscriber,
                                    HttpClient httpClient) {
        this.wikimediaEventsUrl = wikimediaEventsUrl;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.sseHandler = new SSEHandler();
        this.recentChangeEventSubscriber = recentChangeEventSubscriber;
        this.httpClient = httpClient;
    }

    @Override
    public void start() {
        sseHandler.subscribe(recentChangeEventSubscriber);
        executorService.scheduleWithFixedDelay(() -> processEvents(sseHandler), 0L, 10L,
                TimeUnit.MILLISECONDS);
    }

    private void processEvents(final SSEHandler sseHandler) {
        try {
            httpClient.send(buildRequest(), HttpResponse.BodyHandlers.fromLineSubscriber(sseHandler));
        } catch (IOException | InterruptedException e) {
            log.error("Failed to start request for events: ", e);
        }
    }

    private HttpRequest buildRequest() {
        return HttpRequest
                .newBuilder()
                .header("Accept", EVENT_STREAM_MEDIA_TYPE)
                .timeout(Duration.ofMillis(1500))
                .uri(URI.create(wikimediaEventsUrl))
                .build();
    }

    @Override
    public void close() {
        try {
            terminateExecutor();
            terminateHttpClient();
            terminateSseHandler();
        } catch (InterruptedException e) {
            log.error("Failed to shutdown Executor Service", e);
        }
    }

    private void terminateExecutor() throws InterruptedException {
        executorService.shutdown();
        if (!executorService.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
            log.info("Executor Service shut down");
        }
    }

    private void terminateHttpClient() throws InterruptedException {
        httpClient.close();
        if (!httpClient.awaitTermination(Duration.ofMillis(1000))) {
            log.info("Http Client shut down");
        }
    }

    private void terminateSseHandler() {
        sseHandler.close();
        if (sseHandler.isClosed()) {
            log.info("SSE Handler shut down");
        }
    }
}
