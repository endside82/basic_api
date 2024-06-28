package com.endside.api.schedule.constants;

public enum StartEndType {
    NORMAL(0),
    START(1),
    END(2)
    ;
    final int typeNum;
    StartEndType(int typeNum){
        this.typeNum = typeNum;
    }

}
