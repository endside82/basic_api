package com.endside.api.event.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Setter;

@Setter
@Builder
public class SimpleExtDataVo {
    @JsonProperty(value = "sender_id")
    private long senderId;
}
