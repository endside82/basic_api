package com.endside.api.friend.vo;

import com.endside.api.user.constants.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FriendBlockVo {
    @JsonIgnore
    private long id;
    @JsonIgnore
    private long userId;
    private long friendId;
    private LocalDateTime createdAt;
    private UserStatus status;
    private String nickname;
    private String image;
}
