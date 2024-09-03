package org.taulin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record RecentChangeEventDTO(
        @JsonProperty("$schema") String schema,
        @JsonProperty("meta") MetaDTO meta,
        @JsonProperty("id") Long id,
        @JsonProperty("type") String type,
        @JsonProperty("namespace") Integer namespace,
        @JsonProperty("title") String title,
        @JsonProperty("title_url") String titleUrl,
        @JsonProperty("comment") String comment,
        @JsonProperty("timestamp") Long timestamp,
        @JsonProperty("user") String user,
        @JsonProperty("bot") Boolean bot,
        @JsonProperty("notify_url") String notifyUrl,
        @JsonProperty("server_url") String serverUrl,
        @JsonProperty("server_name") String serverName,
        @JsonProperty("server_script_path") String serverScriptPath,
        @JsonProperty("wiki") String wiki,
        @JsonProperty("parsedcomment") String parsedComment,
        @JsonProperty("minor") Boolean minor,
        @JsonProperty("patrolled") Boolean patrolled,
        @JsonProperty("length") RevisionDTO length,
        @JsonProperty("revision") RevisionDTO revision) {
    @Override
    public String toString() {
        return "RecentChangeEvent{" +
                "schema='" + schema + '\'' +
                ", meta=" + meta +
                ", id=" + id +
                ", type='" + type + '\'' +
                ", namespace=" + namespace +
                ", title='" + title + '\'' +
                ", titleUrl='" + titleUrl + '\'' +
                ", comment='" + comment + '\'' +
                ", timestamp=" + timestamp +
                ", user='" + user + '\'' +
                ", bot=" + bot +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", serverUrl='" + serverUrl + '\'' +
                ", serverName='" + serverName + '\'' +
                ", serverScriptPath='" + serverScriptPath + '\'' +
                ", wiki='" + wiki + '\'' +
                ", parsedComment='" + parsedComment + '\'' +
                ", minor=" + minor +
                ", patrolled=" + patrolled +
                ", length=" + length +
                ", revision=" + revision +
                '}';
    }
}
