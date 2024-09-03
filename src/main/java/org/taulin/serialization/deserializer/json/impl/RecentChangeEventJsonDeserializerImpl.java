package org.taulin.serialization.deserializer.json.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.taulin.serialization.deserializer.json.RecentChangeEventJsonDeserializer;
import org.taulin.model.RecentChangeEventDTO;

import java.util.Objects;
import java.util.Optional;

@Slf4j
public class RecentChangeEventJsonDeserializerImpl implements RecentChangeEventJsonDeserializer {
    private final ObjectMapper mapper;

    @Inject
    public RecentChangeEventJsonDeserializerImpl(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<RecentChangeEventDTO> deserialize(String eventJsonStr) {
        if (Objects.isNull(eventJsonStr)) return Optional.empty();

        try {
            return Optional.of(mapper.readValue(eventJsonStr, RecentChangeEventDTO.class));
        } catch (JsonProcessingException e) {
            log.error("Error deserializing event: {}", eventJsonStr, e);
            return Optional.empty();
        }
    }
}
