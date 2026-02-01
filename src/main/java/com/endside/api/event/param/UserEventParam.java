package com.endside.api.event.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEventParam {
    @JsonIgnore
    private long targetId;

    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("limit")
    private int limit;
}