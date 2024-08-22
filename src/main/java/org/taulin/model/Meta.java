package org.taulin.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Meta(
        @JsonProperty("uri") String uri,
        @JsonProperty("request_id") String requestId,
        @JsonProperty("id") String id,
        @JsonProperty("dt") String dt,
        @JsonProperty("domain") String domain,
        @JsonProperty("stream") String stream,
        @JsonProperty("topic") String topic,
        @JsonProperty("partition") Integer partition,
        @JsonProperty("offset") Long offset) {
    @Override
    public String toString() {
        return "Meta{" +
                "uri='" + uri + '\'' +
                ", requestId='" + requestId + '\'' +
                ", id='" + id + '\'' +
                ", dt='" + dt + '\'' +
                ", domain='" + domain + '\'' +
                ", stream='" + stream + '\'' +
                ", topic='" + topic + '\'' +
                ", partition=" + partition +
                ", offset=" + offset +
                '}';
    }
}
