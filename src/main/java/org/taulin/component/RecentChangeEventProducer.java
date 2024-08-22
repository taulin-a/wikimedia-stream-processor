package org.taulin.component;

import org.taulin.model.RecentChangeEvent;

public interface RecentChangeEventProducer {
    void send(RecentChangeEvent recentChangeEvent);
}
