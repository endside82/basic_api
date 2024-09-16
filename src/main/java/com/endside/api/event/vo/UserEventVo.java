package com.endside.api.event.vo;

import com.endside.api.event.model.UserEvent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Setter
public class UserEventVo {
    @JsonProperty("source")
    private String source;
    @JsonProperty("type")
    private String type;
    @JsonProperty("target_id")
    private long targetId;
    @JsonProperty("target_type")
    private int targetType;
    @JsonProperty("ext_data")
    private String extData;
    @JsonIgnore
    private Instant timestamp;
    @JsonProperty("timestamp")
    public long getTimestamp() {
        return this.timestamp.toEpochMilli();
    }

    public List<UserEventVo> mapList(List<UserEvent> userEvents) {
        ModelMapper modelMapper = new ModelMapper();
        List<UserEventVo> userEventVos = userEvents.stream().map(event -> modelMapper.map(event, UserEventVo.class)).collect(Collectors.toList());
        return userEventVos;
    }
}