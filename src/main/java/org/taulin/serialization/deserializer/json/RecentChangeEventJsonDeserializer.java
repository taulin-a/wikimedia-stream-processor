package org.taulin.serialization.deserializer.json;

import org.taulin.model.RecentChangeEventDTO;

import java.util.Optional;

public interface RecentChangeEventJsonDeserializer {
    Optional<RecentChangeEventDTO> deserialize(String eventStr);
}
