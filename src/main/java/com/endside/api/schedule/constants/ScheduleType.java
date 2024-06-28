package com.endside.api.schedule.constants;

public enum ScheduleType {
    WORK(0),                // 워크
    WORK_MAINTENANCE(1) ,   // 워크 + 정비 
    MAINTENANCE(2),         // 정비일
    UNPAID_REST(3),        // 휴식일
    ;
    final int typeNum;
    ScheduleType(int typeNum){
        this.typeNum = typeNum;
    }
}
