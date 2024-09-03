package org.taulin.component;

import org.taulin.model.RecentChangeEventDTO;

import java.util.Optional;

public interface RecentChangeEventDeserializer {
    Optional<RecentChangeEventDTO> deserialize(String eventStr);
}
