package com.endside.api.friend.vo;

import com.endside.api.user.constants.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FriendReqSendVo {
    @JsonIgnore
    private long friendId;
    // friend_request
    @JsonIgnore
    private long id;
    private long receiver;
    private LocalDateTime createdAt; // 만든날짜
    // user
    private String loginId;
    @JsonIgnore
    private UserStatus status;
    // profile
    private String nickname;
    private String image;
}
