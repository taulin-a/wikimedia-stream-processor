package org.taulin.component.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.taulin.component.RecentChangeEventDeserializer;
import org.taulin.model.RecentChangeEvent;

import java.util.Objects;
import java.util.Optional;

@Slf4j
public class RecentChangeEventDeserializerImpl implements RecentChangeEventDeserializer {
    private final ObjectMapper mapper;

    @Inject
    public RecentChangeEventDeserializerImpl(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<RecentChangeEvent> deserialize(String eventStr) {
        if (Objects.isNull(eventStr)) return Optional.empty();

        try {
            return Optional.of(mapper.readValue(eventStr, RecentChangeEvent.class));
        } catch (JsonProcessingException e) {
            log.error("Error deserializing event: {}", eventStr, e);
            return Optional.empty();
        }
    }
}
