package com.endside.api.event.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Setter;

@Setter
@Builder
public class SocietyExtDataVo {
    @JsonProperty(value = "society_id")
    private long societyId;

    @JsonProperty(value = "leader_id")
    private long leaderId;

    @JsonProperty(value = "member_id")
    private long memberId;
}
