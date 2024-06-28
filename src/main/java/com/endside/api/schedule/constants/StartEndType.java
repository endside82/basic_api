package com.endside.api.schedule.constants;

import lombok.Getter;

@Getter
public enum StartEndType {
    NORMAL(0),  // 일반
    START(1),   // 시작
    END(2);     // 종료

    private final int typeNum;

    StartEndType(int typeNum) {
        this.typeNum = typeNum;
    }
}
