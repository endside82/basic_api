package com.endside.api.event.constant;

import lombok.Getter;

public enum UserEventType {
    FRIEND_REQUEST("friend.request"),
    FRIEND_ACCEPT("friend.accept"),
    FRIEND_DELETE("friend.delete"),
    USER_LEAVE("user.leave"),
    ;

    @Getter
    private final String userEventType;
    UserEventType(String userEventType) {
        this.userEventType = userEventType;
    }

    @Override
    public String toString() {
        return this.userEventType;
    }
}