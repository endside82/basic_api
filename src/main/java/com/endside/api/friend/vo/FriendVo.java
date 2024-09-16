package com.endside.api.friend.vo;

import com.endside.api.user.constants.UserStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FriendVo {
    // account(friend)
    private String id;
    private UserStatus status;
    // profile(friend)
    private String nickname;
    private String image;
    // friend
    private long friendId;
    private LocalDateTime createdAt; // 만든날짜
    private LocalDateTime updatedAt; // 변경일
}
