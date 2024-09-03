package org.taulin.component;

import org.taulin.model.RecentChangeEventDTO;

public interface RecentChangeEventProducer {
    void send(RecentChangeEventDTO recentChangeEvent);
}
