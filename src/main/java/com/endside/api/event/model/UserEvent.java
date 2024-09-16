package com.endside.api.event.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class UserEvent {
    @JsonProperty("source")
    private String source;

    @JsonProperty("type")
    private String type;

    @JsonProperty("target_id")
    private long targetId;

    @JsonProperty("target_type")
    private int targetType;

    @JsonProperty("receivers")
    private List<Long> receivers;

    @JsonProperty("ext_data")
    private String extData;

    @JsonProperty("timestamp")
    private Instant timestamp;

    @Builder
    public UserEvent(String source, String type, long targetId, int targetType, String extData, Instant timestamp) {
        this.source = source;
        this.type = type;
        this.targetId = targetId;
        this.targetType = targetType;
        this.extData = extData;
        this.timestamp = timestamp;
    }
}