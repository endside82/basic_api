package com.endside.api.event.constant;

import lombok.Getter;

public enum TargetType {
    SINGLE(0),
    GROUP(1),
    PLURAL(2)
    ;

    @Getter
    private final int targetType;

    TargetType(int targetType) {
        this.targetType = targetType;
    }
}