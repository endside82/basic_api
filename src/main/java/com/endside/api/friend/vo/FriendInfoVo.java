package com.endside.api.friend.vo;

import com.endside.api.user.constants.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FriendInfoVo {

    // account(friend)
    private String id;
    private UserStatus status;

    // profile(friend)
    private String image;
    private String nickname;

    // friend
    private long friendId;
    private LocalDateTime createdAt; // 만든날짜
    private LocalDateTime updatedAt; // 변경일
}
