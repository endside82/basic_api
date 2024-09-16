package com.endside.api.event.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class EventQueueParam {
    @JsonProperty("source")
    private String source;

    @JsonProperty("type")
    private String type;

    @JsonProperty("sender_id")
    private long senderId;

    @JsonProperty("target_id")
    private long targetId;

    @JsonProperty("target_type")
    private int targetType;

    @JsonProperty("receivers")
    private List<Long> receivers;

    @JsonProperty("ext_data")
    private String extData;

    @JsonProperty("timestamp")
    private long timestamp;

    // TODO: delete when using test event producer method is done
    @Builder
    public EventQueueParam(String source, String type, long senderId, long targetId, int targetType, List<Long> receivers, String extData, long timestamp) {
        this.source = source;
        this.type = type;
        this.senderId = senderId;
        this.targetId = targetId;
        this.targetType = targetType;
        this.receivers = receivers;
        this.extData = extData;
        this.timestamp = timestamp;
    }
}