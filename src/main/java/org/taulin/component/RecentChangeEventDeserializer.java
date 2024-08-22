package org.taulin.component;

import org.taulin.model.RecentChangeEvent;

import java.util.Optional;

public interface RecentChangeEventDeserializer {
    Optional<RecentChangeEvent> deserialize(String eventStr);
}
