package com.endside.api.stuff.constants;

public enum StuffStatus {
    STANDBY(0),
    ACTIVE(1),
    INACTIVE(2)
    ;
    private final int value;

    StuffStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
