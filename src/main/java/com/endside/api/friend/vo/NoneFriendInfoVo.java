package com.endside.api.friend.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoneFriendInfoVo {
    // user(none friend)
    // none friend userId
    private long userId;
    // profile(none friend)
    private String image;
    private String nickname;
    private boolean isRequest;
}
