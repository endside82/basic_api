package com.endside.api.friend.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendBlockParam {
    @JsonIgnore
    private long userId;
    private long friendId;
}
