package com.endside.api.friend.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.endside.api.common.param.PagingParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendParam extends PagingParam {
    @JsonIgnore
    private long userId;
}
