package com.endside.api.schedule.param;

import com.endside.api.common.param.PagingParam;
import com.endside.api.schedule.constants.ScheduleType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ScheduleListParam extends PagingParam {
    private Long receiptId;                     // 접수 고유번호
    private Long stuffId;                       // 물건 고유번호
    // scheduleType, scheduleTypes 중 하나만 세팅
    private ScheduleType scheduleType;          // 스케쥴 종류
    private List<ScheduleType> scheduleTypes;   // 스케쥴 종류 리스트
    // date 또는 startDate 나 endDate (혹은 startDate,endDate 둘다) 중 한 그룹만 세팅
    private LocalDate date;                     // 스케쥴 날짜
    private LocalDate startDate;                // 스케쥴 검색용 시작 날짜
    private LocalDate endDate;                  // 스케쥴 검색용 마지막 날짜
}
