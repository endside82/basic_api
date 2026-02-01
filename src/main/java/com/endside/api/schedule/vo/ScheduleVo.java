package com.endside.api.schedule.vo;

import com.endside.api.schedule.constants.ScheduleType;
import com.endside.api.schedule.constants.StartEndType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleVo {
    private long id;                    // 스케쥴 고유번호
    private long stuffId;               // 물건 고유번호
    private long receiptId;             // 접수 고유번호
    private LocalDate date;             // 스케쥴 날짜
    private ScheduleType scheduleType;  // 스케쥴 종류
    private StartEndType startEndType;  // 시작종료구분
    private LocalDateTime createdAt;    // 생성시각
    private LocalDateTime updatedAt;    // 변경시각
}
