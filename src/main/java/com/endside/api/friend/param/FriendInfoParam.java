package com.endside.api.friend.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.endside.api.common.param.PagingParam;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FriendInfoParam {
    @JsonIgnore
    private long userId;
    private long friendId;
}
