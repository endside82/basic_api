package com.endside.api.user.constants;

import lombok.Getter;

@Getter
public enum UserType {
    HOLDEM(0),
    LOD(1),
    ;
    final int type;

    UserType(int type){
        this.type = type;
    }

}
