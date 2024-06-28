package com.endside.api.schedule.param;


import com.endside.api.schedule.constants.ScheduleType;
import com.endside.api.schedule.constants.StartEndType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ScheduleAddParam {
    private long receiptId;             // 접수 고유번호
    private long stuffId;               // 물건 고유번호
    private LocalDate date;             // 스케쥴 날짜
    private ScheduleType scheduleType;  // 스케쥴 종류
    private StartEndType startEndType;  // 시작종료구분
}
