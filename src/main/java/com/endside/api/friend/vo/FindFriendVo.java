package com.endside.api.friend.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindFriendVo {
    private Long userId;
    private String nickname;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String uniqueNickname;
    private String image;
    private boolean isFriend;
    private boolean isRequest;
}
