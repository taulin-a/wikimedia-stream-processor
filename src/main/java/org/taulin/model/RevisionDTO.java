package org.taulin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record RevisionDTO(
        @JsonProperty("old") Long old,
        @JsonProperty("new") Long nu) {
    @Override
    public String toString() {
        return "Revision{" +
                "old=" + old +
                ", nu=" + nu +
                '}';
    }
}
