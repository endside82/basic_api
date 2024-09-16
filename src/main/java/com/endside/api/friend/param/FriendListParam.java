package com.endside.api.friend.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.endside.api.common.param.PagingParam;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
public class FriendListParam extends PagingParam {
    @JsonIgnore
    private long userId;

}
