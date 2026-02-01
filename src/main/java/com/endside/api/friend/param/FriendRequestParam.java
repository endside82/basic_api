package com.endside.api.friend.param;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendRequestParam {
    private long sender;
    private long receiver;
}
