package org.taulin;

import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import org.taulin.component.EventProcessorWorker;
import org.taulin.factory.ProcessorModule;

@Slf4j
public class WikimediaStreamProcessorApp {
    public static void main(String[] args) {
        final Injector injector = Guice.createInjector(new ProcessorModule());
        final EventProcessorWorker eventProcessorWorker = injector.getInstance(EventProcessorWorker.class);
        eventProcessorWorker.start();

        // shutdown hook
        final Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                log.info("Shutting down...");
                eventProcessorWorker.close();

                mainThread.join();
            } catch (Exception e) {
                log.error("Error while shutting down: ", e);
                throw new RuntimeException(e);
            }
        }));
    }
}