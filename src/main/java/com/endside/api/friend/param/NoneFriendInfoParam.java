package com.endside.api.friend.param;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NoneFriendInfoParam {

    private long userId;
    private long noneFriendId;

}
